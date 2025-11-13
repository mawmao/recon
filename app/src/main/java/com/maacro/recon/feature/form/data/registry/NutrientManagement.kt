package com.maacro.recon.feature.form.data.registry

import com.maacro.recon.core.database.model.FormEntryEntity
import com.maacro.recon.core.network.upsert
import com.maacro.recon.core.network.upsertAndGetId
import com.maacro.recon.feature.form.data.mapper.FormMapper
import com.maacro.recon.feature.form.data.registry.tables.TableRegistry
import com.mawmao.recon.forms.model.FieldType
import com.mawmao.recon.forms.model.annotations.FieldSpec
import com.mawmao.recon.forms.model.annotations.FormSpec
import com.mawmao.recon.forms.model.annotations.GroupSpec
import com.mawmao.recon.forms.model.annotations.OptionsSpec
import com.mawmao.recon.forms.model.annotations.SectionSpec
import io.github.jan.supabase.SupabaseClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

@FormSpec(
    label = "Nutrient Management",
    groups = [
        GroupSpec(
            id = "fertilizer_application",
            title = "Fertilizer Application"
        )
    ],
    sections = [
        SectionSpec(
            id = "nutrient_management_01",
            label = "Fertilized Area",
            description = "Track fertilized area and related information"
        ),
        SectionSpec(
            id = "nutrient_management_02",
            groupId = "fertilizer_application",
            label = "Fertilizer Type and Brand",
            description = "Record fertilizer type and brand name"
        ),
        SectionSpec(
            id = "nutrient_management_03",
            groupId = "fertilizer_application",
            label = "Nutrient Contents",
            description = "Record nitrogen, phosphorus, and potassium content"
        ),
        SectionSpec(
            id = "nutrient_management_04",
            groupId = "fertilizer_application",
            label = "Application Details",
            description = "Record amount, unit, and crop stage at application"
        )
    ]
)
@Serializable
data class NutrientManagement(

    val id: Int? = null, // used only for DB

    @SerialName("applied_area_sqm")
    @FieldSpec(
        label = "Fertilized Area (sqm)",
        fieldType = FieldType.NUMBER,
        sectionId = "nutrient_management_01"
    )
    val appliedAreaSqM: Double,

    @SerialName("fertilizer_type")
    @FieldSpec(
        label = "Fertilizer Type",
        fieldType = FieldType.DROPDOWN,
        sectionId = "nutrient_management_02"
    )
    @OptionsSpec(options = ["Organic", "Inorganic", "Compost", "Other"])
    val fertilizerType: String,

    @SerialName("brand")
    @FieldSpec(
        label = "Brand Name",
        fieldType = FieldType.TEXT,
        sectionId = "nutrient_management_02"
    )
    val brandName: String,

    @SerialName("nitrogen_content_pct")
    @FieldSpec(
        label = "Nitrogen Content",
        fieldType = FieldType.NUMBER,
        sectionId = "nutrient_management_03"
    )
    val nitrogenContentPercent: Double,

    @SerialName("phosphorus_content_pct")
    @FieldSpec(
        label = "Phosphorus Content",
        fieldType = FieldType.NUMBER,
        sectionId = "nutrient_management_03"
    )
    val phosphorusContentPercent: Double,

    @SerialName("potassium_content_pct")
    @FieldSpec(
        label = "Potassium Content",
        fieldType = FieldType.NUMBER,
        sectionId = "nutrient_management_03"
    )
    val potassiumContentPercent: Double,

    @SerialName("amount_applied")
    @FieldSpec(
        label = "Amount Applied",
        fieldType = FieldType.NUMBER,
        sectionId = "nutrient_management_04"
    )
    val amountApplied: Double,

    @SerialName("amount_unit")
    @FieldSpec(
        label = "Unit",
        fieldType = FieldType.DROPDOWN,
        sectionId = "nutrient_management_04"
    )
    @OptionsSpec(options = ["kg", "g"]) // NOTE: could add more
    val amountUnit: String,

    @SerialName("crop_stage_on_application")
    @FieldSpec(
        label = "Crop Stage on Application",
        fieldType = FieldType.DROPDOWN,
        sectionId = "nutrient_management_04"
    )
    @OptionsSpec(options = ["Pre-planting", "Vegetative", "Flowering", "Maturity"])
    val cropStageOnApplication: String
)


/**
 *  Nutrient Management is split between tables in central DB (see [FertilizationRecord], [FertilizerApplications])
 */

@Serializable
private data class FertilizationRecord(
    val id: Int? = null, // used only for DB
    @SerialName("applied_area_sqm") val appliedAreaSqM: Double,
    @SerialName("fertilizer_application") val fertilizerApplication: List<FertilizerApplications>? = null
)

@Serializable
private data class FertilizerApplications(
    @SerialName("fertilization_record_id") val fertilizationRecordId: Int? = null,
    @SerialName("fertilizer_type") val fertilizerType: String,
    @SerialName("brand") val brandName: String,
    @SerialName("nitrogen_content_pct") val nitrogenContentPercent: Double,
    @SerialName("phosphorus_content_pct") val phosphorusContentPercent: Double,
    @SerialName("potassium_content_pct") val potassiumContentPercent: Double,
    @SerialName("amount_applied") val amountApplied: Double,
    @SerialName("amount_unit") val amountUnit: String,
    @SerialName("crop_stage_on_application") val cropStageOnApplication: String
)

object NutrientManagementMapper : FormMapper() {
    override suspend fun upload(entry: FormEntryEntity, client: SupabaseClient) {
        val payload = Json.decodeFromString<FertilizationRecord>(entry.payloadJson)
        val parentData = buildParentData(client = client, entry = entry)
        val parentId = client.upsertAndGetId(
            table = TableRegistry.FIELD_ACTIVITIES,
            item = parentData,
            onConflict = "field_id, season_id, activity_type"
        )

        val detailParentId = client.upsertAndGetId(
            table = TableRegistry.FERTILIZATION_RECORDS,
            item = Json.encodeToJsonElement(
                FertilizationRecord(
                    id = parentId,
                    appliedAreaSqM = payload.appliedAreaSqM
                )
            ),
            onConflict = "id"
        )

        payload.fertilizerApplication?.forEach { payload ->
            client.upsert(
                table = TableRegistry.FERTILIZER_APPLICATIONS,
                item = Json.encodeToJsonElement(
                    FertilizerApplications(
                        fertilizationRecordId = detailParentId,
                        fertilizerType = payload.fertilizerType,
                        brandName = payload.brandName,
                        nitrogenContentPercent = payload.nitrogenContentPercent,
                        phosphorusContentPercent = payload.phosphorusContentPercent,
                        potassiumContentPercent = payload.potassiumContentPercent,
                        amountApplied = payload.amountApplied,
                        amountUnit = payload.amountUnit,
                        cropStageOnApplication = payload.cropStageOnApplication,
                    )
                ),
                onConflict = "id"
            )
        }
    }
}
