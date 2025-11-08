package com.maacro.recon.feature.form.data.mapper

import com.maacro.recon.core.database.model.FormEntryEntity
import com.maacro.recon.core.network.getSingleId
import com.maacro.recon.feature.form.data.registry.tables.encodeFieldActivities
import com.maacro.recon.feature.form.data.registry.util.toActivityType
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

abstract class FormMapper {
    abstract suspend fun upload(entry: FormEntryEntity, client: SupabaseClient)

    protected suspend fun buildParentData(
        client: SupabaseClient,
        entry: FormEntryEntity,
    ): JsonElement {
        return withContext(Dispatchers.Default) {

            val fieldId = client.getSingleId("fields") {
                filter { eq("mfid", entry.mfid) }
            }
            val seasonId = client.getSingleId("seasons") {
                order("id", Order.DESCENDING)
            }

            return@withContext Json.encodeFieldActivities(
                fieldId = fieldId,
                seasonId = seasonId,
                activityType = entry.activityType.toActivityType(),
                collectedBy = entry.collectedBy,
                collectedAt = entry.collectedAt,
                imageUrls = emptyList()
            )
        }
    }
}

