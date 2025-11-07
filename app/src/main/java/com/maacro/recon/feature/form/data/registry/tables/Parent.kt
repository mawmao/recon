package com.maacro.recon.feature.form.data.registry.tables

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

fun Json.encodeFieldActivities(
    fieldId: Int,
    seasonId: Int,
    activityType: String,
    collectedBy: String,
    collectedAt: String,
    imageUrls: List<String>
) = this.encodeToJsonElement(
    FieldActivities(
        fieldId = fieldId,
        seasonId = seasonId,
        activityType = activityType,
        collectedBy = collectedBy,
        collectedAt = collectedAt,
        imageUrls = imageUrls
    )
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class FieldActivities(

    @SerialName("field_id")
    val fieldId: Int,

    @SerialName("season_id")
    val seasonId: Int,

    @SerialName("activity_type")
    val activityType: String,

    @SerialName("collected_by")
    val collectedBy: String,

    @SerialName("collected_at")
    val collectedAt: String,

    @SerialName("image_urls")
    @EncodeDefault
    val imageUrls: List<String> = emptyList()
)

