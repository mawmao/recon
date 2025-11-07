package com.maacro.recon.feature.form.data.registry

import com.maacro.recon.core.database.model.FormEntryEntity
import com.maacro.recon.feature.form.data.mapper.FormMapper
import com.mawmao.recon.forms.model.FieldType
import com.mawmao.recon.forms.model.annotations.FieldSpec
import com.mawmao.recon.forms.model.annotations.FormSpec
import com.mawmao.recon.forms.model.annotations.OptionsSpec
import com.mawmao.recon.forms.model.annotations.SectionSpec
import io.github.jan.supabase.SupabaseClient
import kotlinx.serialization.Serializable

@FormSpec(
    label = "Cultural Management",
    sections = [
        SectionSpec(
            id = "land_preparation_basic",
            label = "Land Preparation",
            description = "Record land preparation date and soil type"
        ),
        SectionSpec(
            id = "field_conditions_and_area",
            label = "Field Conditions & Area",
            description = "Record current field condition and area measurements"
        ),
        SectionSpec(
            id = "land_prep_image",
            label = "Land Preparation Image",
            description = "Upload image for land preparation"
        ),
        SectionSpec(
            id = "planned_establishment_basic",
            label = "Planned Crop Establishment",
            description = "Record planned date and method of crop establishment"
        ),
        SectionSpec(
            id = "crop_info",
            label = "Crop Information",
            description = "Select crop planted and its current status"
        ),
        SectionSpec(
            id = "planned_establishment_image",
            label = "Planned Establishment Image",
            description = "Upload image for planned crop establishment"
        ),
        SectionSpec(
            id = "actual_establishment_basic",
            label = "Actual Crop Establishment",
            description = "Record actual date and method of crop establishment"
        ),
        SectionSpec(
            id = "plant_spacing",
            label = "Plant Spacing Details",
            description = "Record seedling age and spacing between/within plants"
        ),
        SectionSpec(
            id = "seeding_details",
            label = "Seeding Details",
            description = "Record seeding rate and direct-seeded method"
        ),
        SectionSpec(
            id = "actual_establishment_image",
            label = "Actual Establishment Image",
            description = "Upload image for actual crop establishment"
        ),
        SectionSpec(
            id = "variety_info",
            label = "Rice Variety Information",
            description = "Enter rice variety, number, and maturity duration"
        ),
        SectionSpec(
            id = "seed_source",
            label = "Seed Type and Source",
            description = "Select seed type and source of good seeds"
        ),
        SectionSpec(
            id = "ecosystem_irrigation",
            label = "Ecosystem and Irrigation",
            description = "Identify field ecosystem and irrigation details"
        ),
        SectionSpec(
            id = "irrigation_image",
            label = "Irrigation Image",
            description = "Upload image of irrigation source or setup"
        )
    ]
)
@Serializable
data class CulturalManagement(

    @FieldSpec(
        label = "Land Preparation Date",
        fieldType = FieldType.DATE,
        sectionId = "land_preparation_basic"
    )
    val landPrepDate: Long? = null,

    @FieldSpec(
        label = "Soil Type",
        fieldType = FieldType.DROPDOWN,
        sectionId = "land_preparation_basic"
    )
    @OptionsSpec(options = ["Clayey", "Loamy", "Sandy", "Silty", "Peaty"])
    val soilType: String? = null,

    @FieldSpec(
        label = "Current Field Condition",
        fieldType = FieldType.DROPDOWN,
        sectionId = "field_conditions_and_area"
    )
    @OptionsSpec(options = ["Dry", "Wet", "Flooded", "Weedy", "Leveled"])
    val currentFieldCondition: String? = null,

    @FieldSpec(
        label = "Total Field Area (ha)",
        fieldType = FieldType.NUMBER,
        sectionId = "field_conditions_and_area"
    )
    val totalFieldAreaHa: Double? = null,

    @FieldSpec(
        label = "Monitoring Field Area (m²)",
        fieldType = FieldType.NUMBER,
        sectionId = "field_conditions_and_area"
    )
    val monitoringFieldAreaSqM: Double? = null,

    @FieldSpec(
        label = "Upload Image (Land Preparation)",
        fieldType = FieldType.TEXT,
        sectionId = "land_prep_image"
    )
    val landPrepImageUrl: String? = null,

    @FieldSpec(
        label = "Planned Crop Establishment Date",
        fieldType = FieldType.DATE,
        sectionId = "planned_establishment_basic"
    )
    val plannedCropEstablishmentDate: Long? = null,

    @FieldSpec(
        label = "Planned Crop Establishment Method",
        fieldType = FieldType.DROPDOWN,
        sectionId = "planned_establishment_basic"
    )
    @OptionsSpec(options = ["Direct-seeded", "Transplanted"])
    val plannedCropEstablishmentMethod: String? = null,

    @FieldSpec(
        label = "Crop Planted",
        fieldType = FieldType.DROPDOWN,
        sectionId = "crop_info"
    )
    @OptionsSpec(options = ["Rice", "Corn", "Other"])
    val cropPlanted: String? = null,

    @FieldSpec(
        label = "Crop Status",
        fieldType = FieldType.DROPDOWN,
        sectionId = "crop_info"
    )
    @OptionsSpec(options = ["Not Yet Planted", "Growing", "Harvested"])
    val cropStatus: String? = null,

    @FieldSpec(
        label = "Upload Image (Planned Crop Establishment)",
        fieldType = FieldType.TEXT,
        sectionId = "planned_establishment_image"
    )
    val plannedEstablishmentImageUrl: String? = null,

    @FieldSpec(
        label = "Actual Crop Establishment Date",
        fieldType = FieldType.DATE,
        sectionId = "actual_establishment_basic"
    )
    val actualCropEstablishmentDate: Long? = null,

    @FieldSpec(
        label = "Actual Crop Establishment Method",
        fieldType = FieldType.DROPDOWN,
        sectionId = "actual_establishment_basic"
    )
    @OptionsSpec(options = ["Direct-seeded", "Transplanted"])
    val actualCropEstablishmentMethod: String? = null,

    @FieldSpec(
        label = "Seedling Age at Transplanting (days)",
        fieldType = FieldType.NUMBER,
        sectionId = "plant_spacing"
    )
    val seedlingAgeAtTransplanting: Int? = null,

    @FieldSpec(
        label = "Average Distance Between Rice Plants (cm)",
        fieldType = FieldType.NUMBER,
        sectionId = "plant_spacing"
    )
    val avgPlantDistanceBetween: Double? = null,

    @FieldSpec(
        label = "Average Distance Within Rice Plants (cm)",
        fieldType = FieldType.NUMBER,
        sectionId = "plant_spacing"
    )
    val avgPlantDistanceWithin: Double? = null,

    @FieldSpec(
        label = "Seeding Rate (kg/ha)",
        fieldType = FieldType.NUMBER,
        sectionId = "seeding_details"
    )
    val seedingRate: Double? = null,

    @FieldSpec(
        label = "Direct Seeded Method",
        fieldType = FieldType.DROPDOWN,
        sectionId = "seeding_details"
    )
    @OptionsSpec(options = ["Broadcast", "Drum Seeder", "Line Sowing"])
    val directSeededMethod: String? = null,

    @FieldSpec(
        label = "Upload Image (Actual Crop Establishment)",
        fieldType = FieldType.TEXT,
        sectionId = "actual_establishment_image"
    )
    val actualEstablishmentImageUrl: String? = null,

    @FieldSpec(
        label = "Rice Variety",
        fieldType = FieldType.DROPDOWN,
        sectionId = "variety_info"
    )
    @OptionsSpec(options = ["NSIC Rc 160", "NSIC Rc 222", "PSB Rc 82", "Other"])
    val riceVariety: String? = null,

    @FieldSpec(
        label = "Rice Variety Number",
        fieldType = FieldType.NUMBER,
        sectionId = "variety_info"
    )
    val riceVarietyNo: Int? = null,

    @FieldSpec(
        label = "Rice Maturity Duration (days)",
        fieldType = FieldType.NUMBER,
        sectionId = "variety_info"
    )
    val riceMaturityDuration: Int? = null,

    @FieldSpec(
        label = "Seed Type",
        fieldType = FieldType.DROPDOWN,
        sectionId = "seed_source"
    )
    @OptionsSpec(options = ["Certified", "Hybrid", "Good Seed", "Farmer-saved"])
    val seedType: String? = null,

    @FieldSpec(
        label = "Source of Good Seeds",
        fieldType = FieldType.DROPDOWN,
        sectionId = "seed_source"
    )
    @OptionsSpec(options = ["DA", "Local Supplier", "Farmer Cooperative", "Others"])
    val sourceOfGoodSeeds: String? = null,

    @FieldSpec(
        label = "Type of Ecosystem",
        fieldType = FieldType.DROPDOWN,
        sectionId = "ecosystem_irrigation"
    )
    @OptionsSpec(options = ["Irrigated Lowland", "Rainfed Lowland", "Upland"])
    val typeOfEcosystem: String? = null,

    @FieldSpec(
        label = "Source of Irrigation",
        fieldType = FieldType.DROPDOWN,
        sectionId = "ecosystem_irrigation"
    )
    @OptionsSpec(options = ["Irrigation Canal", "Rainfed", "Pump", "River", "Others"])
    val sourceOfIrrigation: String? = null,

    @FieldSpec(
        label = "Type of Irrigation",
        fieldType = FieldType.DROPDOWN,
        sectionId = "ecosystem_irrigation"
    )
    @OptionsSpec(options = ["Continuous", "Intermittent", "Alternate Wetting and Drying"])
    val typeOfIrrigation: String? = null,

    @FieldSpec(
        label = "Upload Image (Irrigation Source)",
        fieldType = FieldType.TEXT,
        sectionId = "irrigation_image"
    )
    val irrigationImageUrl: String? = null
)


object CulturalManagementMapper : FormMapper() {
    override suspend fun upload(entry: FormEntryEntity, client: SupabaseClient) {
        TODO("Not yet implemented")
    }
}