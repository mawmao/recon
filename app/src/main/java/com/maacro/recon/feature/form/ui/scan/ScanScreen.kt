package com.maacro.recon.feature.form.ui.scan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maacro.recon.feature.form.ui.components.BarcodeScannerView
import com.maacro.recon.ui.components.ReconButton
import com.maacro.recon.ui.components.ReconTopAppBar
import com.maacro.recon.ui.util.safePadding

/**
 * TODO -
 * Could possibly remove view model because scanned barcode is handled in the [ConfirmScreen]
 */

@Composable
fun ScanScreen(
    vm: ScanViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onBarcodeScan: (String) -> Unit
) {
    val state by vm.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // temp
    ) {

        ReconButton(
            text = "Simulate Scan",
            onClick = { onBarcodeScan("hehe")}
        )
//        BarcodeScannerView(
//            modifier = Modifier.fillMaxSize(),
//            onBarcodeDetected = { barcode ->
//                vm.onAction(ScanAction.BarcodeScanned(barcode))
//                barcode?.let {
//                    onBarcodeScan(it)
//                }
//            }
//        )

        Column(
            modifier = Modifier
                .safePadding()
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            ReconTopAppBar(
                onNavigateBack = onNavigateBack,
                contentColor = Color.White,
                actions = {}
            )
        }

        Text(
            text = state.detectedBarcode ?: "",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 128.dp)
        )
    }
}