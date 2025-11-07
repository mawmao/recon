package com.maacro.recon.feature.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maacro.recon.core.common.converter.isoDisplay
import com.maacro.recon.ui.components.ReconIconButton
import com.maacro.recon.ui.components.ReconTopAppBar
import com.maacro.recon.ui.util.safePadding
import java.util.Date


@Composable
fun HistoryScreen(
    vm: HistoryViewModel = hiltViewModel()
) {

    val state by vm.state.collectAsStateWithLifecycle()
    val events = vm.events

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safePadding(),
        verticalArrangement = Arrangement.Top,
    ) {
        HistoryScreenTopBar(
            onClear = vm::clearDatabase
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.entries) { entry ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text("ID: ${entry.id}")
                    Text("MFID: ${entry.mfid}")
                    Text("Type: ${entry.activityType}")
                    Text("Collected by: ${entry.collectedBy}")
                    Text("Collected at: ${entry.collectedAt}")
                    Text("Synced: ${entry.synced}")
                    Text("Payload: ${entry.payloadJson}")
                }
            }
        }
    }
}

@Composable
private fun HistoryScreenTopBar(
    modifier: Modifier = Modifier,
    onClear: () -> Unit
) {
    ReconTopAppBar(
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "History",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        actions = {
            ReconIconButton(
                onClick = onClear,
                imageVector = Icons.Filled.ClearAll,
                contentDescription = "Clear All Database Icon"
            )
        }
    )
}
