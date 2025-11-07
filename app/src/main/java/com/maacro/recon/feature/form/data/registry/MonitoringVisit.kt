package com.maacro.recon.feature.form.data.registry

import com.maacro.recon.core.database.model.FormEntryEntity
import com.maacro.recon.core.network.upsert
import com.maacro.recon.core.network.upsertAndGetId
import com.maacro.recon.feature.form.data.mapper.FormMapper
import com.maacro.recon.feature.form.data.registry.tables.TableRegistry
import com.mawmao.recon.forms.model.FieldType.DATE
import com.mawmao.recon.forms.model.FieldType.DROPDOWN
import com.mawmao.recon.forms.model.FieldType.NUMBER
import com.mawmao.recon.forms.model.annotations.FieldSpec
import com.mawmao.recon.forms.model.annotations.FormSpec
import com.mawmao.recon.forms.model.annotations.OptionsSpec
import com.mawmao.recon.forms.model.annotations.SectionSpec
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.encodeToJsonElement


@FormSpec(
    label = "Monitoring Visit",
    sections = [
        SectionSpec(
            id = "monitoring_01",
            label = "Monitoring Date",
            description = "Record the date of the field monitoring visit"
        ),
        SectionSpec(
            id = "monitoring_02",
            label = "Conditions",
            description = "Record crop status, soil moisture, and plant height"
        )
    ]
)
@Serializable
data class MonitoringVisit(

    val id: Int? = null, // used only for DB

    @SerialName("date_monitored")
    @FieldSpec(sectionId = "monitoring_01", label = "Date Monitored", fieldType = DATE)
    val dateMonitored: String,

    @SerialName("crop_stage")
    @FieldSpec(sectionId = "monitoring_02", label = "Crop Stage", fieldType = DROPDOWN)
    @OptionsSpec(options = ["Not Yet Planted", "Emerging", "Vegetative", "Flowering", "Harvest Ready"])
    val cropStage: String,

    @SerialName("soil_moisture_status")
    @FieldSpec(sectionId = "monitoring_02", label = "Soil Moisture Status", fieldType = DROPDOWN)
    @OptionsSpec(options = ["Dry", "Moist", "Wet", "Flooded"])
    val soilMoistureStatus: String,

    @SerialName("avg_plant_height")
    @FieldSpec(sectionId = "monitoring_02", label = "Average Plant Height", fieldType = NUMBER)
    val avgPlantHeight: Double

)

object MonitoringVisitMapper : FormMapper() {
    override suspend fun upload(entry: FormEntryEntity, client: SupabaseClient) {
        val payload = Json.decodeFromString<MonitoringVisit>(entry.payloadJson)
        val parentData = buildParentData(client = client, entry = entry)
        val parentId = client.upsertAndGetId(
            table = TableRegistry.FIELD_ACTIVITIES,
            item = parentData,
            onConflict = "field_id, season_id, activity_type"
        )

        client.upsert(
            table = TableRegistry.MONITORING_VISITS,
            item = Json.encodeToJsonElement(payload.copy(id = parentId)),
            onConflict = "id"
        )
    }
}
