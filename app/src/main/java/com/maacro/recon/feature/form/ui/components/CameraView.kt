package com.maacro.recon.feature.form.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.maacro.recon.core.system.BarcodeAnalyzer
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun BarcodeScannerView(
    modifier: Modifier = Modifier,
    requestPermissionBeforeShown: Boolean = true,
    onBarcodeDetected: (barcode: String?) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val previewView = remember {
        PreviewView(context)
    }

    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            hasPermission = granted
        }
    LaunchedEffect(Unit) {
        if (!hasPermission && requestPermissionBeforeShown) launcher.launch(Manifest.permission.CAMERA)
    }

    Box(modifier = modifier) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        DisposableEffect(lifecycleOwner, cameraProviderFuture) {
            val cameraProvider = try {
                cameraProviderFuture.get()
            } catch (t: Exception) {
                onDispose { }
                throw t
            }

            // build use-cases (no Camera2Interop AF_OFF here)
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_DEFAULT)
                .build()
                .also { it.surfaceProvider = previewView.surfaceProvider }

            val selector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setTargetResolution(Size(1280, 720))
                .build()

            val analyzer = BarcodeAnalyzer(onBarcodeDetected = { onBarcodeDetected(it.rawValue) })
            imageAnalysis.setAnalyzer(analysisExecutor, analyzer)

            // handler to schedule delayed focus
            val handler = Handler(Looper.getMainLooper())
            var camera: Camera?
            val focusDelayMs = 2000L

            try {
                camera =
                    cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview, imageAnalysis)

                // cancel any ongoing AF to start "unfocused"
                try {
                    camera.cameraControl.cancelFocusAndMetering()
                } catch (_: Exception) {
                }

                // schedule focusing after delay (center point)
                val focusRunnable = Runnable {
                    try {
                        // use center of previewView; fallback if size 0
                        val w = if (previewView.width > 0) previewView.width.toFloat() else 1f
                        val h = if (previewView.height > 0) previewView.height.toFloat() else 1f
                        val pointFactory = SurfaceOrientedMeteringPointFactory(w, h)
                        val center = pointFactory.createPoint(w / 2f, h / 2f)
                        val action =
                            FocusMeteringAction.Builder(center, FocusMeteringAction.FLAG_AF)
                                .setAutoCancelDuration(3, TimeUnit.SECONDS) // optional
                                .build()
                        camera.cameraControl.startFocusAndMetering(action)
                    } catch (_: Exception) {
                    }
                }
                handler.postDelayed(focusRunnable, focusDelayMs)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            onDispose {
                handler.removeCallbacksAndMessages(null)
                try {
                    cameraProvider.unbindAll()
                } catch (_: Exception) {
                }
                try {
                    analyzer.release()
                } catch (_: Exception) {
                }
            }
        }
    }
}
