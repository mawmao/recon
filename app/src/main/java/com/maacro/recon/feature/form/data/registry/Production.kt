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
    label = "Production",
    sections = [
        SectionSpec(
            id = "production_01",
            label = "Harvest Details",
            description = "Record harvest date and method used"
        ),
        SectionSpec(
            id = "production_02",
            label = "Harvest Yield (Bags)",
            description = "Record number of bags harvested and average bag weight"
        ),
        SectionSpec(
            id = "production_03",
            label = "Harvested Area and Irrigation",
            description = "Record harvested area and irrigation adequacy during season"
        )
    ]
)
@Serializable
data class Production(

    val id: Int? = null, // used only for DB

    @SerialName("harvest_date")
    @FieldSpec(
        label = "Harvest Date",
        fieldType = FieldType.DATE,
        sectionId = "production_01"
    )
    val harvestDate: String,

    @SerialName("harvesting_method")
    @FieldSpec(
        label = "Harvesting Method",
        fieldType = FieldType.DROPDOWN,
        sectionId = "production_01"
    )
    @OptionsSpec(options = ["Manual", "Mechanical", "Other"])
    val harvestingMethod: String,

    @SerialName("bags_harvested")
    @FieldSpec(
        label = "Bags Harvested",
        fieldType = FieldType.NUMBER,
        sectionId = "production_02"
    )
    val bagsHarvested: Int,

    @SerialName("avg_bag_weight_kg")
    @FieldSpec(
        label = "Average Bag Weight (kg)",
        fieldType = FieldType.NUMBER,
        sectionId = "production_02"
    )
    val avgBagWeightKg: Double,

    @SerialName("area_harvested_ha")
    @FieldSpec(
        label = "Harvested Area (ha)",
        fieldType = FieldType.NUMBER,
        sectionId = "production_03"
    )
    val areaHarvestedHa: Double,

    @SerialName("irrigation_supply")
    @FieldSpec(
        label = "Irrigation Supply",
        fieldType = FieldType.DROPDOWN,
        sectionId = "production_03"
    )
    @OptionsSpec(options = ["Not Enough", "Not Sufficient", "Sufficient", "Excessive"])
    val irrigationSupply: String
)

object ProductionMapper : FormMapper() {
    override suspend fun upload(entry: FormEntryEntity, client: SupabaseClient) {
        val payload = Json.decodeFromString<Production>(entry.payloadJson)
        val parentData = buildParentData(client = client, entry = entry)
        val parentId = client.upsertAndGetId(
            table = TableRegistry.FIELD_ACTIVITIES,
            item = parentData,
            onConflict = "field_id, season_id, activity_type"
        )

        client.upsert(
            table = TableRegistry.HARVEST_RECORDS,
            item = Json.encodeToJsonElement(payload.copy(id = parentId)),
            onConflict = "id"
        )
    }
}