package com.maacro.recon.feature.form.ui.scan

import androidx.lifecycle.ViewModel
import com.maacro.recon.core.domain.VMActions
import com.maacro.recon.core.domain.VMState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor() : ViewModel(), VMActions<ScanAction> {

    private val _state = VMState(ScanScreenState())
    val state = _state.flow

    override fun onAction(action: ScanAction) {
        when (action) {
            is ScanAction.BarcodeScanned -> {
                _state.update { state -> state.copy(detectedBarcode = action.barcode) }
            }
        }
    }
}

data class ScanScreenState(
    val detectedBarcode: String? = null
)

sealed class ScanAction {
    data class BarcodeScanned(val barcode: String?) : ScanAction()
}
