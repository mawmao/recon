package com.maacro.recon.navigation.auth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.maacro.recon.feature.auth.ui.login.LoginScreen
import com.maacro.recon.feature.fp.OtpScreen
import com.maacro.recon.navigation.util.transitionComposable
import com.maacro.recon.ui.ReconAppState
import com.maacro.recon.ui.sections.AuthSectionState

@Composable
fun ReconAuthNavHost(
    appState: ReconAppState,
    authSectionState: AuthSectionState
) {
    NavHost(
        navController = authSectionState.navController,
        startDestination = AuthSection.Login
    ) {
        transitionComposable<AuthSection.Login> {
            LoginScreen(
                onAuthSuccess = appState::navigateToMain,
                onForgotPassword = authSectionState::navigateToForgotPassword,
            )
        }

        navigation<AuthSection.ForgotPassword>(
            startDestination = AuthSection.ForgotPassword.Otp
        ) {
            transitionComposable<AuthSection.ForgotPassword.Otp> {
                OtpScreen(onNavigateBack = authSectionState::navigateBack)
            }
        }
    }
}
