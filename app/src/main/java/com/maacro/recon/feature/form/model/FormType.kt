package com.maacro.recon.feature.form.model

import androidx.compose.ui.util.fastFilter
import androidx.compose.ui.util.fastFirst
import com.maacro.recon.feature.form.data.mapper.FormMapper
import com.maacro.recon.feature.form.data.registry.CulturalManagementForm
import com.maacro.recon.feature.form.data.registry.CulturalManagementMapper
import com.maacro.recon.feature.form.data.registry.DamageAssessmentForm
import com.maacro.recon.feature.form.data.registry.DamageAssessmentMapper
import com.maacro.recon.feature.form.data.registry.FarmerProfileForm
import com.maacro.recon.feature.form.data.registry.FarmerProfileMapper
import com.maacro.recon.feature.form.data.registry.MonitoringVisitForm
import com.maacro.recon.feature.form.data.registry.MonitoringVisitMapper
import com.maacro.recon.feature.form.data.registry.NutrientManagementForm
import com.maacro.recon.feature.form.data.registry.NutrientManagementMapper
import com.maacro.recon.feature.form.data.registry.ProductionForm
import com.maacro.recon.feature.form.data.registry.ProductionMapper
import com.maacro.recon.feature.form.data.registry.util.toActivityType
import com.mawmao.recon.forms.model.Form

enum class FormCategory { CORE, OPTIONAL }
enum class FormType(
    val label: String,
    val category: FormCategory,
    val template: Form = Form(label = "empty", elements = emptyList()),
    val mapper: FormMapper,
) {
    PROFILE(
        label = "Farmer Profile",
        category = FormCategory.CORE,
        template = FarmerProfileForm,
        mapper = FarmerProfileMapper
    ),

    CULTURAL_MANAGEMENT(
        label = "Cultural Management",
        category = FormCategory.CORE,
        template = CulturalManagementForm,
        mapper = CulturalManagementMapper
    ),

    NUTRIENT_MANAGEMENT(
        label = "Nutrient Management",
        category = FormCategory.CORE,
        template = NutrientManagementForm,
        mapper = NutrientManagementMapper,
    ),

    PRODUCTION(
        label = "Production",
        category = FormCategory.CORE,
        template = ProductionForm,
        mapper = ProductionMapper,
    ),

    MONITORING_VISIT(
        label = "Monitoring Visit",
        category = FormCategory.OPTIONAL,
        template = MonitoringVisitForm,
        mapper = MonitoringVisitMapper
    ),

    DAMAGE_ASSESSMENT(
        label = "Damage Assessment",
        category = FormCategory.OPTIONAL,
        template = DamageAssessmentForm,
        mapper = DamageAssessmentMapper
    );

    companion object {
        val coreTypes get() = entries.fastFilter { it.category == FormCategory.CORE }
        val optionalTypes get() = entries.fastFilter { it.category == FormCategory.OPTIONAL }

        fun fromActivityType(activityTypeName: String): FormType =
            entries.fastFirst { it.name.toActivityType().equals(activityTypeName, ignoreCase = true) }

        fun fromName(formTypeName: String): FormType =
            entries.fastFirst { it.name.equals(formTypeName, ignoreCase = true) }
    }
}

