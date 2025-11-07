package com.maacro.recon.ui.sections

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.maacro.recon.feature.form.model.FieldValue
import com.maacro.recon.feature.form.ui.question.FormAnswers
import com.maacro.recon.navigation.form.FormSection
import com.maacro.recon.navigation.form.ReconFormNavHost
import com.maacro.recon.ui.ReconAppState
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun FormSection(
    appState: ReconAppState,
    formType: String,
    formSectionState: FormSectionState = rememberFormSectionState(formType = formType),
) {
    ReconFormNavHost(
        appState = appState,
        formSectionState = formSectionState,
    )
}

@Composable
fun rememberFormSectionState(
    formType: String,
    formNavController: NavHostController = rememberNavController(),
) = remember(formNavController, formType) {
    FormSectionState(navController = formNavController, formType = formType)
}

@Stable
class FormSectionState(
    val navController: NavHostController,
    val formType: String,
) {

    fun navigateToQuestions(barcode: String) {
        navController.navigate(FormSection.Question(barcode, formType)) {
            launchSingleTop = true
        }
    }

    fun navigateToReview(barcode: String, answers: Map<String, FieldValue>) {
        val answersJson = Json.encodeToString(answers)
        navController.navigate(
            FormSection.Review(
                mfid = barcode,
                formTypeName = formType,
                answersJson = answersJson
            )
        ) {
            launchSingleTop = true
        }
    }

    fun navigateToConfirm(barcode: String) {
        navController.navigate(FormSection.Confirm(barcode)) {
            launchSingleTop = true
        }
    }

    fun navigateBack() {
        navController.navigateUp() // testing; change back to popBackStack if problem arises
    }
}
