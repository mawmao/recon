package com.maacro.recon.core.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.request.SelectRequestBuilder
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

inline fun <reified T : Any> PostgrestResult.decodeFirstOrDefault(): T? {
    return try {
        decodeSingle<T>()
    } catch (e: NoSuchElementException) {
        null
    }
}

suspend fun SupabaseClient.getSingleId(
    table: String,
    block: SelectRequestBuilder.() -> Unit = {}
): Int {
    val arr = from(table)
        .select(Columns.list("id"), block)
        .decodeSingle<JsonObject>()

    return arr["id"]?.jsonPrimitive?.int ?: error("Missing ID in table $table")
}

suspend inline fun <reified T : Any> SupabaseClient.upsert(
    table: String,
    item: T,
    onConflict: String
) {
    this.from(table).upsert(item) { this.onConflict = onConflict }
}

suspend inline fun <reified T> SupabaseClient.upsertAndGetId(
    table: String,
    item: T,
    onConflict: String
): Int {
    val jsonElement = Json.encodeToJsonElement(item)

    val result = from(table)
        .upsert(JsonArray(listOf(jsonElement))) {
            this.onConflict = onConflict
            select()
        }
        .decodeSingle<JsonObject>()

    return result["id"]?.jsonPrimitive?.int ?: error("Missing ID after upsert in table $table")
}
