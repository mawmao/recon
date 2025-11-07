package com.maacro.recon.core.network.service

import com.maacro.recon.core.database.model.FormEntryEntity
import com.maacro.recon.feature.form.model.FormType
import io.github.jan.supabase.SupabaseClient
import jakarta.inject.Inject

class SupabaseService @Inject constructor(private val client: SupabaseClient) {
    suspend fun uploadForm(entry: FormEntryEntity) {
        FormType.fromActivityType(entry.activityType)
            .mapper
            .upload(entry, client)
    }
}
