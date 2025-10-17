package com.maacro.recon.feature.form.ui.confirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maacro.recon.ui.components.ReconButton
import com.maacro.recon.ui.components.ReconIconButton
import com.maacro.recon.ui.components.ReconTopAppBar
import com.maacro.recon.ui.util.safePadding

@Composable
fun ConfirmScreen(
    modifier: Modifier = Modifier,
    scannedBarcode: String,
    onContinue: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onExit: () -> Unit
) {

    Column(
        modifier = modifier
            .safePadding()
            .fillMaxSize()
    ) {
        ReconTopAppBar(
            onNavigateBack = onNavigateBack,
            actions = {
                ReconIconButton(
                    onClick = onExit,
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close Icon Button",
                )
            }
        )
        Column(
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Barcode: $scannedBarcode")
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReconButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Continue",
                    onClick = { onContinue(scannedBarcode) }
                )
            }
        }
    }
}


