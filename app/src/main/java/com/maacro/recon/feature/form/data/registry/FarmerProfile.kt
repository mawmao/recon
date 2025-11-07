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
    label = "Farmer Profile",
    sections = [
        SectionSpec(
            id = "farmer_profile_01",
            label = "Farmer Information",
            description = "Basic personal info for quick identification"
        ),
        SectionSpec(
            id = "field_profile_02",
            label = "Field Details",
            description = "Define location and administrative info of the field"
        ),
    ]
)
@Serializable
data class FarmerProfile(

    val id: Int? = null, // used only for DB

    @FieldSpec(
        label = "First Name",
        fieldType = FieldType.TEXT,
        sectionId = "farmer_profile_01",
    )
    val firstName: String,

    @FieldSpec(
        label = "Last Name",
        fieldType = FieldType.TEXT,
        sectionId = "farmer_profile_01",
    )
    val lastName: String,

    @FieldSpec(
        label = "Birthdate",
        fieldType = FieldType.DATE,
        sectionId = "farmer_profile_01",
    )
    val birthdate: Long,

    @FieldSpec(
        label = "Gender",
        fieldType = FieldType.DROPDOWN,
        sectionId = "farmer_profile_01",
    )
    @OptionsSpec(options = ["Male", "Female", "Other"])
    val gender: String,

    @FieldSpec(
        label = "Phone Number",
        fieldType = FieldType.TEXT,
        sectionId = "farmer_profile_01",
    )
    val phoneNo: String? = null,

    @FieldSpec(
        label = "Field Location (GPS)",
        fieldType = FieldType.TEXT,
        sectionId = "field_profile_02",
    )
    val coordinates: String,

    @FieldSpec(
        label = "Province",
        fieldType = FieldType.DROPDOWN,
        sectionId = "field_profile_02",
    )
    val province: String,

    @FieldSpec(
        label = "Municipality or City",
        fieldType = FieldType.DROPDOWN,
        sectionId = "field_profile_02",
    )
    val municipalityOrCity: String,

    @FieldSpec(
        label = "Barangay",
        fieldType = FieldType.DROPDOWN,
        sectionId = "field_profile_02",
    )
    val barangay: String
)

object FarmerProfileMapper : FormMapper() {
    override suspend fun upload(entry: FormEntryEntity, client: SupabaseClient) {
        TODO("Not yet implemented")
    }
}