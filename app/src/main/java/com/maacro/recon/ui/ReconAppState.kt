package com.maacro.recon.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.maacro.recon.feature.form.model.FormType
import com.maacro.recon.navigation.RootSection

@Composable
fun rememberReconAppState(
    navController: NavHostController = rememberNavController()
): ReconAppState {
    return remember(navController) {
        ReconAppState(
            navController = navController,
        )
    }
}

@Stable
class ReconAppState(val navController: NavHostController) {

    fun popToMain() {
        navController.popBackStack(
            route = RootSection.Main,
            inclusive = false,
            saveState = false
        )
    }

    fun navigateToAuth() = navigateRoot(RootSection.Auth)
    fun navigateToMain() = navigateRoot(RootSection.Main)
    fun navigateToForm(formType: FormType) {
        navController.navigate(RootSection.Form(formTypeName = formType.name))
    }

    private fun navigateRoot(root: RootSection) {
        navController.navigate(route = root) {
            popUpTo(0) { inclusive = true }
        }
    }
}

