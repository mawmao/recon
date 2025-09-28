package com.maacro.recon.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
data object HomeRoute


fun NavGraphBuilder.homeGraph(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit
) {
    composable<HomeRoute> {
        HomeScreen(
            modifier = modifier,
            onSignOut = onSignOut // temporary
        )
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit, // temporary, should be in a viewmodel or something
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        ReconButton(
            text = "Sign Out",
            onClick = onSignOut, // temp
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
