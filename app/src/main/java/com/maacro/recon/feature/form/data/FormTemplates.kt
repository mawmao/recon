package com.maacro.recon.feature.form.data

import android.R.attr.type
import com.maacro.recon.feature.form.model.Field
import com.maacro.recon.feature.form.model.FieldRow
import com.maacro.recon.feature.form.model.FieldType
import com.maacro.recon.feature.form.model.Form
import com.maacro.recon.feature.form.model.FormType.CULTURAL
import com.maacro.recon.feature.form.model.FormType.DAMAGE
import com.maacro.recon.feature.form.model.FormType.MONITORING
import com.maacro.recon.feature.form.model.FormType.NUTRIENT
import com.maacro.recon.feature.form.model.FormType.PRODUCTION
import com.maacro.recon.feature.form.model.FormType.PROFILE
import com.maacro.recon.feature.form.model.Section


object FormTemplates {

    fun getFormByType(type: String): Form? = when (type) {
        PROFILE -> Profile
        CULTURAL -> CulturalManagement
        NUTRIENT -> NutrientManagement
        PRODUCTION -> Production
        MONITORING -> MonitoringVisits
        DAMAGE -> DamageAssessment
        else -> null
    }

    val Profile = Form(
        type = "profile",
        sections = listOf(
            Section(
                id = "farmer_profile_01",
                title = "Farmer Information",
                description = "Basic personal info for quick identification",
                fields = listOf(
                    FieldRow(
                        field = Field(
                            key = "fname",
                            label = "First Name",
                            type = FieldType.TEXT,
                            placeholder = "Enter first name",
                            required = true
                        )
                    ),
                    FieldRow(
                        field = Field(
                            key = "lname",
                            label = "Last Name",
                            type = FieldType.TEXT,
                            placeholder = "Enter last name",
                            required = true
                        )
                    ),
                    FieldRow(
                        field = Field(
                            key = "birthdate",
                            label = "Birthdate",
                            type = FieldType.DATE,
                            placeholder = "Select birthdate",
                            required = true
                        )
                    ),
                )
            ),
            Section(
                id = "farmer_profile_02",
                title = "Farmer Information",
                description = "Basic personal info for quick identification",
                fields = listOf(
                    FieldRow(
                        field = Field(
                            key = "gender",
                            label = "Gender",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select gender",
                            required = true,
                            options = listOf("Male", "Female", "Other")
                        )
                    ),
                    FieldRow(
                        field = Field(
                            key = "phone_no",
                            label = "Phone Number",
                            type = FieldType.TEXT,
                            placeholder = "09xxxxxxxxx"
                        )
                    )
                )
            ),
            Section(
                id = "field_profile_03",
                title = "Field Details",
                description = "Define location and administrative info of the field",
                fields = listOf(
                    FieldRow(
                        fields = listOf(
                            Field(
                                key = "coordinates",
                                label = "Field Location (GPS)",
                                type = FieldType.TEXT,
                                placeholder = "Tap map to pin location",
                                required = true
                            )
                        )
                    ),
                )
            ),
            Section(
                id = "field_profile_04",
                title = "Field Details",
                description = "Define location and administrative info of the field",
                fields = listOf(
                    FieldRow(
                        field = Field(
                            key = "province",
                            label = "Province",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select province",
                            required = true,
                            options = listOf("provinces.json")
                        )
                    ),
                    FieldRow(
                        field = Field(
                            key = "municipality_or_city",
                            label = "Municipality or City",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select municipality or city",
                            required = true,
                            options = listOf("municipalities.json")
                        )
                    ),
                    FieldRow(
                        field = Field(
                            key = "barangay",
                            label = "Barangay",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select barangay",
                            required = true,
                            options = listOf("barangays.json")
                        )
                    )
                )
            )
        )
    )

    val CulturalManagement = Form(
        type = "cultural_management",
        sections = listOf(
            // Section 1
            Section(
                id = "land_preparation_basic",
                title = "Land Preparation",
                description = "Record land preparation date and soil type",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "land_prep_date",
                            label = "Land Preparation Date",
                            type = FieldType.DATE,
                            placeholder = "Select land preparation date"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "soil_type",
                            label = "Soil Type",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select soil type",
                            options = listOf("Clayey", "Loamy", "Sandy", "Silty", "Peaty")
                        )
                    )
                )
            ),

            // Section 2
            Section(
                id = "field_conditions_and_area",
                title = "Field Conditions & Area",
                description = "Record current field condition and area measurements",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "current_field_conditions",
                            label = "Current Field Condition",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select field condition",
                            options = listOf("Dry", "Wet", "Flooded", "Weedy", "Leveled")
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "total_field_area_ha",
                            label = "Total Field Area (ha)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter total area in hectares"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "monitoring_field_area_sqm",
                            label = "Monitoring Field Area (m²)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter monitored area in square meters"
                        )
                    )
                )
            ),

            // Section 3
            Section(
                id = "land_prep_image",
                title = "Land Preparation Image",
                description = "Upload image for land preparation",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "image_url",
                            label = "Upload Image (Land Preparation)",
                            type = FieldType.TEXT,
                            placeholder = "Paste image URL or upload reference"
                        )
                    )
                )
            ),

            // Section 4
            Section(
                id = "planned_establishment_basic",
                title = "Planned Crop Establishment",
                description = "Record planned date and method of crop establishment",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "planned_crop_establishment_date",
                            label = "Planned Crop Establishment Date",
                            type = FieldType.DATE,
                            placeholder = "Select planned date"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "planned_crop_establishment_method",
                            label = "Planned Crop Establishment Method",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select method",
                            options = listOf("Direct-seeded", "Transplanted")
                        )
                    )
                )
            ),

            // Section 5
            Section(
                id = "crop_info",
                title = "Crop Information",
                description = "Select crop planted and its current status",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "crop_planted",
                            label = "Crop Planted",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select crop",
                            options = listOf("Rice", "Corn", "Other")
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "crop_status",
                            label = "Crop Status",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select crop status",
                            options = listOf("Not Yet Planted", "Growing", "Harvested")
                        )
                    )
                )
            ),

            // Section 6
            Section(
                id = "planned_establishment_image",
                title = "Planned Establishment Image",
                description = "Upload image for planned crop establishment",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "image_url",
                            label = "Upload Image (Planned Crop Establishment)",
                            type = FieldType.TEXT,
                            placeholder = "Paste image URL or upload reference"
                        )
                    )
                )
            ),

            // Section 7
            Section(
                id = "actual_establishment_basic",
                title = "Actual Crop Establishment",
                description = "Record actual date and method of crop establishment",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "actual_crop_establishment_date",
                            label = "Actual Crop Establishment Date",
                            type = FieldType.DATE,
                            placeholder = "Select actual date"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "actual_crop_establishment_method",
                            label = "Actual Crop Establishment Method",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select method",
                            options = listOf("Direct-seeded", "Transplanted")
                        )
                    )
                )
            ),

            // Section 8
            Section(
                id = "plant_spacing",
                title = "Plant Spacing Details",
                description = "Record seedling age and spacing between/within plants",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "seedling_age_at_transplanting",
                            label = "Seedling Age at Transplanting (days)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter age in days"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "avg_plant_distance_between",
                            label = "Average Distance Between Rice Plants (cm)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter spacing between rows"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "avg_plant_distance_within",
                            label = "Average Distance Within Rice Plants (cm)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter spacing within rows"
                        )
                    )
                )
            ),

            // Section 9
            Section(
                id = "seeding_details",
                title = "Seeding Details",
                description = "Record seeding rate and direct-seeded method",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "seeding_rate",
                            label = "Seeding Rate (kg/ha)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter seeding rate"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "direct_seeded_method",
                            label = "Direct Seeded Method",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select method",
                            options = listOf("Broadcast", "Drum Seeder", "Line Sowing")
                        )
                    )
                )
            ),

            // Section 10
            Section(
                id = "actual_establishment_image",
                title = "Actual Establishment Image",
                description = "Upload image for actual crop establishment",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "image_url",
                            label = "Upload Image (Actual Crop Establishment)",
                            type = FieldType.TEXT,
                            placeholder = "Paste image URL or upload reference"
                        )
                    )
                )
            ),

            // Section 11
            Section(
                id = "variety_info",
                title = "Rice Variety Information",
                description = "Enter rice variety, number, and maturity duration",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "rice_variety",
                            label = "Rice Variety",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select rice variety",
                            options = listOf("NSIC Rc 160", "NSIC Rc 222", "PSB Rc 82", "Other")
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "rice_variety_no",
                            label = "Rice Variety Number",
                            type = FieldType.NUMBER,
                            placeholder = "Enter variety number"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "rice_maturity_duration",
                            label = "Rice Maturity Duration (days)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter maturity duration"
                        )
                    )
                )
            ),

            // Section 12
            Section(
                id = "seed_source",
                title = "Seed Type and Source",
                description = "Select seed type and source of good seeds",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "seed_type",
                            label = "Seed Type",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select seed type",
                            options = listOf("Certified", "Hybrid", "Good Seed", "Farmer-saved")
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "source_of_good_seeds",
                            label = "Source of Good Seeds",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select source",
                            options = listOf(
                                "DA",
                                "Local Supplier",
                                "Farmer Cooperative",
                                "Others"
                            )
                        )
                    )
                )
            ),

            // Section 13
            Section(
                id = "ecosystem_irrigation",
                title = "Ecosystem and Irrigation",
                description = "Identify field ecosystem and irrigation details",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "type_of_ecosystem",
                            label = "Type of Ecosystem",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select ecosystem type",
                            options = listOf("Irrigated Lowland", "Rainfed Lowland", "Upland")
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "source_of_irrigation",
                            label = "Source of Irrigation",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select source of irrigation",
                            options = listOf(
                                "Irrigation Canal",
                                "Rainfed",
                                "Pump",
                                "River",
                                "Others"
                            )
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "type_of_irrigation",
                            label = "Type of Irrigation",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select irrigation type",
                            options = listOf(
                                "Continuous",
                                "Intermittent",
                                "Alternate Wetting and Drying"
                            )
                        )
                    )
                )
            ),

            // Section 14
            Section(
                id = "irrigation_image",
                title = "Irrigation Image",
                description = "Upload image of irrigation source or setup",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "image_url",
                            label = "Upload Image (Irrigation Source)",
                            type = FieldType.TEXT,
                            placeholder = "Paste image URL or upload reference"
                        )
                    )
                )
            )
        )
    )

    val NutrientManagement = Form(
        type = "nutrient_management",
        sections = listOf(
            // Page 1: Fertilized Area
            Section(
                id = "fertilized_area",
                title = "Fertilized Area",
                description = "Track fertilized area and related information",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "fertilized_area_sqm",
                            label = "Fertilized Area (sqm)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter total fertilized area in sqm"
                        )
                    )
                )
            ),

            // Page 2: Fertilizer Type & Brand
            Section(
                id = "fertilizer_type_brand",
                title = "Fertilizer Type and Brand",
                description = "Record fertilizer type and brand name",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "type",
                            label = "Fertilizer Type",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select fertilizer type",
                            options = listOf("Organic", "Inorganic", "Compost", "Other")
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "brand_name",
                            label = "Brand Name",
                            type = FieldType.TEXT,
                            placeholder = "Enter brand name"
                        )
                    )
                )
            ),

            // Page 3: Nutrient Contents
            Section(
                id = "nutrient_contents",
                title = "Nutrient Contents",
                description = "Record nitrogen, phosphorus, and potassium content",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "nitrogen_content",
                            label = "Nitrogen Content",
                            type = FieldType.NUMBER,
                            placeholder = "Enter % nitrogen content"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "phosphorus_content",
                            label = "Phosphorus Content",
                            type = FieldType.NUMBER,
                            placeholder = "Enter % phosphorus content"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "potassium_content",
                            label = "Potassium Content",
                            type = FieldType.NUMBER,
                            placeholder = "Enter % potassium content"
                        )
                    )
                )
            ),

            // Page 4: Application Details
            Section(
                id = "application_details",
                title = "Application Details",
                description = "Record amount, unit, and crop stage at application",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "amount",
                            label = "Amount Used",
                            type = FieldType.NUMBER,
                            placeholder = "Enter amount used"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "unit",
                            label = "Unit",
                            type = FieldType.TEXT,
                            placeholder = "Enter unit (e.g. kg, L)"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "crop_stage_on_application",
                            label = "Crop Stage on Application",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select crop stage",
                            options = listOf(
                                "Pre-planting",
                                "Vegetative",
                                "Flowering",
                                "Maturity"
                            )
                        )
                    )
                )
            )
        )
    )

    val Production = Form(
        type = "production",
        sections = listOf(
            // Page 1: Harvest Date & Method
            Section(
                id = "harvest_basic",
                title = "Harvest Details",
                description = "Record harvest date and method used",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "harvest_date",
                            label = "Harvest Date",
                            type = FieldType.DATE,
                            placeholder = "Select harvest date"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "harvest_method",
                            label = "Harvest Method",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select harvest method",
                            options = listOf("Manual", "Mechanical", "Other")
                        )
                    )
                )
            ),

            // Page 2: Yield - Bags & Weight
            Section(
                id = "harvest_yield_bags",
                title = "Harvest Yield (Bags)",
                description = "Record number of bags harvested and average bag weight",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "bags_harvested",
                            label = "Bags Harvested",
                            type = FieldType.NUMBER,
                            placeholder = "Enter total number of bags"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "avg_bag_weight_kg",
                            label = "Average Bag Weight (kg)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter average weight per bag"
                        )
                    )
                )
            ),

            // Page 3: Area & Irrigation Adequacy
            Section(
                id = "harvest_area_irrigation",
                title = "Harvested Area and Irrigation",
                description = "Record harvested area and irrigation adequacy during season",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "harvested_area_ha",
                            label = "Harvested Area (ha)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter harvested area in hectares"
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "irrigation_adequacy",
                            label = "Irrigation Adequacy",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select irrigation adequacy",
                            options = listOf("Adequate", "Moderate", "Inadequate")
                        )
                    )
                )
            )
        )
    )

    val MonitoringVisits = Form(
        type = "monitoring_visit",
        sections = listOf(
            // Section 1: Date Monitored
            Section(
                id = "monitoring_date",
                title = "Monitoring Date",
                description = "Record the date of the field monitoring visit",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "date_monitored",
                            label = "Date Monitored",
                            type = FieldType.DATE,
                            placeholder = "Select date of visit"
                        )
                    )
                )
            ),

            // Section 2: Crop & Field Conditions
            Section(
                id = "monitoring_conditions",
                title = "Crop and Soil Conditions",
                description = "Record crop status, soil moisture, and plant height",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "crop_status",
                            label = "Crop Status",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select crop status",
                            options = listOf(
                                "Not Yet Planted",
                                "Emerging",
                                "Vegetative",
                                "Flowering",
                                "Harvest Ready"
                            )
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "soil_moisture_status",
                            label = "Soil Moisture Status",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select soil moisture condition",
                            options = listOf("Dry", "Moist", "Wet", "Flooded")
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "avg_plant_height",
                            label = "Average Plant Height (cm)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter average height in cm"
                        )
                    )
                )
            )
        )
    )

    val DamageAssessment = Form(
        type = "damage_assessment",
        sections = listOf(
            // Section 1: Crop Stage & Soil Type
            Section(
                id = "crop_and_soil",
                title = "Crop Stage and Soil Type",
                description = "Record the crop growth stage and soil type at time of damage",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "crop_stage",
                            label = "Crop Stage",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select crop stage",
                            options = listOf(
                                "Seedling",
                                "Vegetative",
                                "Reproductive",
                                "Maturity"
                            )
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "soil_type",
                            label = "Soil Type",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select soil type",
                            options = listOf("Clayey", "Loamy", "Sandy", "Silty", "Peaty")
                        )
                    )
                )
            ),

            // Section 2: Cause & Observed Pest
            Section(
                id = "damage_cause",
                title = "Cause of Damage",
                description = "Identify the cause and observed pest or agent",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "cause",
                            label = "Cause of Damage",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select cause of damage",
                            options = listOf(
                                "Pest",
                                "Disease",
                                "Flood",
                                "Drought",
                                "Wind",
                                "Other"
                            )
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "observed_pest",
                            label = "Observed Pest",
                            type = FieldType.TEXT,
                            placeholder = "Enter observed pest or causal agent"
                        )
                    )
                )
            ),

            // Section 3: Severity & Affected Area
            Section(
                id = "damage_impact",
                title = "Damage Impact",
                description = "Record severity level and affected area",
                fields = listOf(
                    FieldRow(
                        Field(
                            key = "severity",
                            label = "Severity",
                            type = FieldType.DROPDOWN,
                            placeholder = "Select damage severity",
                            options = listOf("Low", "Moderate", "High", "Severe")
                        )
                    ),
                    FieldRow(
                        Field(
                            key = "affected_area_ha",
                            label = "Affected Area (ha)",
                            type = FieldType.NUMBER,
                            placeholder = "Enter affected area in hectares"
                        )
                    )
                )
            )
        )
    )

}
