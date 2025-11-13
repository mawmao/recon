package com.maacro.recon.navigation.form

import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.maacro.recon.core.common.ReconRoute
import com.maacro.recon.feature.form.model.CollectionStatus
import com.maacro.recon.navigation.RootSection
import com.maacro.recon.navigation.util.transitionComposable
import com.maacro.recon.ui.ReconAppState
import com.maacro.recon.ui.components.Monitor
import com.maacro.recon.ui.sections.FormSection
import com.maacro.recon.ui.sections.rememberFormSectionState
import kotlinx.serialization.Serializable

@Serializable
sealed class FormSection : ReconRoute {

    @Serializable
    data object Scan : FormSection()

    @Serializable
    data object Confirm : FormSection()

    @Serializable
    sealed class Question : FormSection() {

        @Serializable
        data object Root: Question()

        @Serializable
        data object Basic : Question()

        @Serializable
        data object Async : Question()
    }

    @Serializable
    data object Review : FormSection()
}

fun NavGraphBuilder.formNavigation(appState: ReconAppState) {
    transitionComposable<RootSection.Form> {
        val form: RootSection.Form = it.toRoute()
        val formSectionState = rememberFormSectionState(formType = form.formTypeName)

        Monitor(
            onCompose = { CollectionStatus.Log.i(CollectionStatus.Collect.Start(form.formTypeName)) },
            onDispose = { CollectionStatus.Log.v(CollectionStatus.Collect.End(form.formTypeName)) }
        ) {
            FormSection(appState = appState, formSectionState = formSectionState)
        }
    }
}
