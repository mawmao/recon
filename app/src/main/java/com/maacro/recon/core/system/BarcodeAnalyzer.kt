package com.maacro.recon.core.system

import android.os.Looper
import android.os.SystemClock
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
class BarcodeAnalyzer(
    private val onBarcodeDetected: (barcode: Barcode) -> Unit,
    private val cooldownMs: Long = 1500L // throttle duplicate callbacks
) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    private val scanner = BarcodeScanning.getClient(options)
    @OptIn(ExperimentalAtomicApi::class)
    private val isProcessing = AtomicBoolean(false)

    @Volatile
    private var lastDetected: String? = null

    @Volatile
    private var lastDetectedAt = 0L

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        // skip if previous frame still processing
        if (!isProcessing.compareAndSet(false, true)) {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                val barcode = barcodes.firstOrNull()
                if (barcode != null) {
                    val raw = barcode.rawValue
                    val now = SystemClock.elapsedRealtime()
                    // simple debounce: only emit if different or cooldown expired
                    val shouldEmit =
                        raw != null && (raw != lastDetected || now - lastDetectedAt > cooldownMs)
                    if (shouldEmit) {
                        lastDetected = raw
                        lastDetectedAt = now
                        // ensure callback runs on main thread
                        android.os.Handler(Looper.getMainLooper()).post { onBarcodeDetected(barcode) }
                    }
                }
            }
            .addOnFailureListener { it.printStackTrace() }
            .addOnCompleteListener {
                isProcessing.set(false)
                imageProxy.close()
            }
    }

    fun release() {
        try {
            scanner.close()
        } catch (t: Throwable) { /* ignore */
        }
    }
}
