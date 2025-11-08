package com.maacro.recon

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.maacro.recon.feature.auth.data.AuthRepository
import com.maacro.recon.ui.ReconApp
import com.maacro.recon.ui.theme.ReconTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var keepSplash = true
        splashScreen.setKeepOnScreenCondition { keepSplash }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
        )

        setContent {
            val sessionStatus by authRepository.sessionStatus.collectAsState(initial = null)

            LaunchedEffect(sessionStatus) {
                if (sessionStatus != null) keepSplash = false
            }

            sessionStatus?.let {
                ReconTheme {
                    ReconApp(
                        sessionStatus = it
                    )
                }
            }
        }
    }
}