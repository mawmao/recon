package com.maacro.recon.feature.form.data.registry

import com.maacro.recon.core.database.model.FormEntryEntity
import com.maacro.recon.feature.form.data.CoordinatesRepository
import com.maacro.recon.feature.form.data.LocationRepository
import com.maacro.recon.feature.form.data.mapper.FormMapper
import com.mawmao.recon.forms.model.FieldType
import com.mawmao.recon.forms.model.annotations.FieldSpec
import com.mawmao.recon.forms.model.annotations.FormSpec
import com.mawmao.recon.forms.model.annotations.OptionsSpec
import com.mawmao.recon.forms.model.annotations.SectionSpec
import io.github.jan.supabase.SupabaseClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@FormSpec(
    label = "Farmer Profile",
    sections = [
        SectionSpec(
            id = "farmer_info",
            label = "Farmer Information",
            description = "Basic personal info for quick identification"
        ),
        SectionSpec(
            id = "personal_details",
            label = "Personal Details",
            description = "Additional personal info"
        ),
        SectionSpec(
            id = "field_timing",
            label = "Field Timing",
            description = "Important dates for crop planning"
        ),
        SectionSpec(
            id = "field_area",
            label = "Field Area",
            description = "Size of the field"
        ),
        SectionSpec(
            id = "field_condition",
            label = "Field Condition",
            description = "Current state of the field"
        ),
        SectionSpec(
            id = "crop_info",
            label = "Crop Information",
            description = "Information about planted crop"
        ),
        SectionSpec(
            id = "field_location",
            label = "Field Location",
            description = "Administrative info"
        ),
        SectionSpec(
            id = "gps_coordinates",
            label = "GPS Coordinates",
            description = "Exact location of the field"
        )
    ]
)

@Serializable
data class FarmerProfile(

    val id: Int? = null, // used only for DB

    @FieldSpec(label = "First Name", fieldType = FieldType.TEXT, sectionId = "farmer_info")
    val firstName: String,

    @FieldSpec(label = "Last Name", fieldType = FieldType.TEXT, sectionId = "farmer_info")
    val lastName: String,

    @FieldSpec(label = "Gender", fieldType = FieldType.DROPDOWN, sectionId = "farmer_info")
    @OptionsSpec(options = ["Male", "Female", "Other"])
    val gender: String,

    @FieldSpec(label = "Date of Birth", fieldType = FieldType.DATE, sectionId = "personal_details")
    val dateOfBirth: String,

    @FieldSpec(label = "Cellphone Number", fieldType = FieldType.TEXT, sectionId = "personal_details")
    val cellPhoneNo: String? = null,

    @FieldSpec(
        label = "Land Preparation Date",
        fieldType = FieldType.DATE,
        sectionId = "field_timing"
    )
    val sosDate: String,

    @FieldSpec(
        label = "Estimated Crop Establishment Date",
        fieldType = FieldType.DATE,
        sectionId = "field_timing"
    )
    val estCropEstablishmentDate: String,

    @FieldSpec(
        label = "Estimated Method of Establishment",
        fieldType = FieldType.TEXT,
        sectionId = "field_timing"
    )
    val estMethodOfEstablishment: String? = null,

    @FieldSpec(
        label = "Total Field Area (ha)",
        fieldType = FieldType.NUMBER,
        sectionId = "field_area"
    )
    val totalFieldAreaHa: Double = 0.0,

    @FieldSpec(label = "Ecosystem", fieldType = FieldType.DROPDOWN, sectionId = "field_condition")
    @OptionsSpec(options = ["Rainfed Lowland", "Irrigation"])
    val ecosystem: String,

    @FieldSpec(label = "Soil Type", fieldType = FieldType.DROPDOWN, sectionId = "field_condition")
    @OptionsSpec(options = ["Clayey", "Loamy", "Sandy", "Silty"])
    val soilType: String,

    @FieldSpec(
        label = "Current Field Condition",
        fieldType = FieldType.DROPDOWN,
        sectionId = "field_condition"
    )
    @OptionsSpec(options = ["Fallow", "Just Harvested", "Planted", "Land Preparation"])
    val currentFieldCondition: String,

    @FieldSpec(label = "Crop Planted", fieldType = FieldType.DROPDOWN, sectionId = "crop_info")
    @OptionsSpec(options = ["Rice"])
    val cropPlanted: String,

    @FieldSpec(label = "Crop Status", fieldType = FieldType.DROPDOWN, sectionId = "crop_info")
    @OptionsSpec(options = ["Seeding", "Tillering", "Germination"])
    val cropStatus: String,

    @FieldSpec(label = "Province", fieldType = FieldType.DROPDOWN, sectionId = "field_location")
    @OptionsSpec(options = ["Aklan", "Antique", "Capiz", "Guimaras", "Iloilo", "Negros Occidental"])
    val province: String,

    @FieldSpec(
        label = "Municipality or City",
        fieldType = FieldType.SEARCHABLE_DROPDOWN,
        sectionId = "field_location",
        dependsOn = "province"
    )
    @OptionsSpec(
        repoClass = LocationRepository::class,
        fetchByParentFunction = "getMunicipalitiesByProvince",
    )
    val municipalityOrCity: String,

    @FieldSpec(
        label = "Barangay",
        fieldType = FieldType.SEARCHABLE_DROPDOWN,
        sectionId = "field_location",
        dependsOn = "municipalityOrCity"
    )
    @OptionsSpec(
        repoClass = LocationRepository::class,
        fetchByParentFunction = "getBarangaysByCityMunicipality",
    )
    val barangay: String,

    @FieldSpec(
        label = "Field Location (GPS)",
        fieldType = FieldType.GPS,
        sectionId = "gps_coordinates"
    )
    @OptionsSpec(
        repoClass = CoordinatesRepository::class,
        fetchAllFunction = "getCoordinatesGeom",
        fetchByParentFunction = ""
    )
    val coordinates: String
)


/**
 *  Farmer Profile is split between tables in central DB (see [Farmers], [Fields], [FieldPlannings])
 */

@Serializable
data class Farmers(
    val id: Int? = null, // used only for DB
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    val gender: String,
    @SerialName("date_of_birth") val dateOfBirth: String,
    @SerialName("cellphone_no") val cellPhoneNo: String
)

data class Fields(
    val id: Int? = null, // used only for DB
    @SerialName("farmer_id") val farmerId: Int,
    @SerialName("barangay_id") val barangayId: Int,
    val mfid: String,
    val location: String, // should insert as `POINT(longitude latitude)` (no comma)
)

data class FieldPlannings(
    val id: Int? = null, // used only for DB
    @SerialName("land_preparation_start_date") val landPreparationStartDate: String,
    @SerialName("est_crop_establishment_date") val estCropEstablishmentDate: String,
    @SerialName("est_crop_establishment_method") val estMethodOfEstablishment: String? = null,
    @SerialName("total_field_area_ha") val totalFieldAreaHa: Double,
    val ecosystem: String,
    @SerialName("soil_type") val soilType: String,
    @SerialName("current_field_condition") val currentFieldCondition: String,
    @SerialName("crop_planted") val cropPlanted: String,
    @SerialName("crop_status") val cropStatus: String,
)


object FarmerProfileMapper : FormMapper() {
    override suspend fun upload(entry: FormEntryEntity, client: SupabaseClient) {
        TODO("Not yet implemented")
    }
}