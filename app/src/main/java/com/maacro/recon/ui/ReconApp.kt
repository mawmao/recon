package com.maacro.recon.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maacro.recon.core.designsystem.components.ReconNavigationBar
import com.maacro.recon.core.designsystem.components.ReconNavigationBarItem
import com.maacro.recon.core.designsystem.theme.Transitions
import com.maacro.recon.navigation.ReconNavHost

@Composable
fun ReconApp(
    appState: ReconAppState = rememberReconAppState()
) {


    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = appState.isTopLevelDestination,
                enter = Transitions.BarEnter,
                exit = Transitions.BarExit
            ) {
                ReconNavigationBar {
                    val currentDestination = appState.currentDestination

                    appState.destinations.forEach { destination ->
                        ReconNavigationBarItem(
                            selected = currentDestination == destination,
                            onClick = {
                                if (currentDestination != destination) {
                                    appState.navigateToTopLevel(destination)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Circle,
                                    contentDescription = destination.label
                                )
                            },
                            selectedIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Circle,
                                    contentDescription = destination.label
                                )
                            },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }
        }
    ) {
        ReconNavHost(
            appState = appState,
            scaffoldPadding = it,
            modifier = // each screen's modifier
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
        )
    }

}
