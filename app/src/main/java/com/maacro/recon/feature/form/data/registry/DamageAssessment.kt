package com.maacro.recon.feature.form.data.registry

import com.maacro.recon.core.database.model.FormEntryEntity
import com.maacro.recon.core.network.upsert
import com.maacro.recon.core.network.upsertAndGetId
import com.maacro.recon.feature.form.data.mapper.FormMapper
import com.maacro.recon.feature.form.data.registry.tables.TableRegistry
import com.mawmao.recon.forms.model.FieldType
import com.mawmao.recon.forms.model.annotations.FieldSpec
import com.mawmao.recon.forms.model.annotations.FormSpec
import com.mawmao.recon.forms.model.annotations.OptionsSpec
import com.mawmao.recon.forms.model.annotations.SectionSpec
import io.github.jan.supabase.SupabaseClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

@FormSpec(
    label = "Damage Assessment",
    sections = [
        SectionSpec(
            id = "damage_assessment_01",
            label = "Crop Stage and Soil Type",
            description = "Record the crop growth stage and soil type at time of damage"
        ),
        SectionSpec(
            id = "damage_assessment_02",
            label = "Cause of Damage",
            description = "Identify the cause and observed pest or agent"
        ),
        SectionSpec(
            id = "damage_assessment_03",
            label = "Damage Impact",
            description = "Record severity level and affected area"
        )
    ]
)
@Serializable
data class DamageAssessment(

    val id: Int? = null, // used only for DB

    @SerialName("crop_stage")
    @FieldSpec(
        label = "Crop Stage",
        fieldType = FieldType.DROPDOWN,
        sectionId = "damage_assessment_01"
    )
    @OptionsSpec(options = ["Seedling", "Vegetative", "Reproductive", "Maturity"])
    val cropStage: String,

    @SerialName("soil_type")
    @FieldSpec(
        label = "Soil Type",
        fieldType = FieldType.DROPDOWN,
        sectionId = "damage_assessment_01"
    )
    @OptionsSpec(options = ["Clayey", "Loamy", "Sandy", "Silty", "Peaty"])
    val soilType: String,

    @SerialName("observed_pest")
    @FieldSpec(
        fieldType = FieldType.TEXT,
        label = "Observed Pest",
        sectionId = "damage_assessment_02",
    )
    val observedPest: String,

    @FieldSpec(
        label = "Cause of Damage",
        fieldType = FieldType.DROPDOWN,
        sectionId = "damage_assessment_02"
    )
    @OptionsSpec(options = ["Pest", "Disease", "Flood", "Drought", "Wind", "Other"])
    val cause: String,

    @FieldSpec(
        label = "Severity",
        fieldType = FieldType.DROPDOWN,
        sectionId = "damage_assessment_03"
    )
    @OptionsSpec(options = ["Low", "Moderate", "High", "Severe"])
    val severity: String,

    @SerialName("affected_area_ha")
    @FieldSpec(
        label = "Affected Area (ha)",
        fieldType = FieldType.NUMBER,
        sectionId = "damage_assessment_03"
    )
    val affectedAreaHa: Double,
)

object DamageAssessmentMapper : FormMapper() {
    override suspend fun upload(entry: FormEntryEntity, client: SupabaseClient) {
        val payload = Json.decodeFromString<DamageAssessment>(entry.payloadJson)
        val parentData = buildParentData(client = client, entry = entry)
        val parentId = client.upsertAndGetId(
            table = TableRegistry.FIELD_ACTIVITIES,
            item = parentData,
            onConflict = "field_id, season_id, activity_type"
        )

        client.upsert(
            table = TableRegistry.DAMAGE_ASSESSMENTS,
            item = Json.encodeToJsonElement(payload.copy(id = parentId)),
            onConflict = "id"
        )
    }
}