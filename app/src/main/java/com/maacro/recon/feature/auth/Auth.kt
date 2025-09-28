package com.maacro.recon.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.maacro.recon.core.designsystem.components.ReconButton
import kotlinx.serialization.Serializable

@Serializable
data object AuthRoute

fun NavGraphBuilder.authGraph(
    modifier: Modifier = Modifier,
    onAuthSuccess: () -> Unit,
) {
    composable<AuthRoute> {
        AuthScreen(modifier, onSignIn = onAuthSuccess)
    }
}

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    onSignIn: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        ReconButton(
            text = "Sign In",
            onClick = onSignIn,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            suffixIcon = {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Outlined.ArrowOutward,
                    contentDescription = null
                )
            }
        )
    }
}