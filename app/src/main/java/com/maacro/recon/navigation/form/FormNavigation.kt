package com.maacro.recon.navigation.form

import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.maacro.recon.core.common.ReconRoute
import com.maacro.recon.navigation.RootSection
import com.maacro.recon.navigation.util.transitionComposable
import com.maacro.recon.ui.ReconAppState
import com.maacro.recon.ui.sections.FormSection
import kotlinx.serialization.Serializable

@Serializable
sealed class FormSection : ReconRoute {

    @Serializable
    data object Scan : FormSection()

    @Serializable
    data class Confirm(val barcode: String) : FormSection()

    @Serializable
    data class Question(val barcode: String, val type: String) : FormSection()
}

fun NavGraphBuilder.formNavigation(appState: ReconAppState) {
    transitionComposable<RootSection.Form> {
        val form: RootSection.Form = it.toRoute()
        FormSection(appState = appState, formType = form.type)
    }
}
