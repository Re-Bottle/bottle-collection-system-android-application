package com.cynthia.bottle_collection_system_android_application.home

import android.util.Log
import android.util.Size
import android.view.Surface
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.cynthia.bottle_collection_system_android_application.R
import com.cynthia.bottle_collection_system_android_application.viewmodel.MainViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


@OptIn(ExperimentalGetImage::class)
@Composable
fun ScanComposable(viewModel: MainViewModel, navigateBack: () -> Unit, userId: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val barcodeScanner = BarcodeScanning.getClient()

    val isScanComplete by viewModel.isScanComplete.collectAsState()
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var lastScannedCode = remember { "" }

    LaunchedEffect(Unit) {
        viewModel.resetScanState()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { viewContext ->
                val previewView = PreviewView(viewContext).apply {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build()
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setTargetRotation(Surface.ROTATION_0)
                        .setTargetResolution(Size(640, 480))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(viewContext)) { imageProxy ->
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val imageInfo = imageProxy.imageInfo
                            val inputImage = InputImage.fromMediaImage(
                                mediaImage,
                                imageInfo.rotationDegrees
                            )

                            barcodeScanner.process(inputImage)
                                .addOnSuccessListener { barcodes ->
                                    for (barcode in barcodes) {
                                        val displayValue = barcode.displayValue
                                        if (!displayValue.isNullOrEmpty() && displayValue != lastScannedCode) {
                                            lastScannedCode = displayValue
                                            Toast.makeText(
                                                context,
                                                "Scanned: $displayValue",
                                                Toast.LENGTH_SHORT
                                            ).show()
// TODO: fix isScanComplete to how a popup or animation
                                            viewModel.claimScan(
                                                userId = userId,
                                                scanData = displayValue,

                                                onError = { errorMessage ->
                                                    println("Error while claiming: $errorMessage")
                                                },
                                                onSuccess = {

                                                })
                                        }
                                    }
                                }
                                .addOnFailureListener {
                                    Log.d("Scan Activity", "Error: ${it.message}")
                                }
                                .addOnCompleteListener {
                                    imageProxy.close()
                                }
                        } else {
                            imageProxy.close()
                        }
                    }

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalysis
                    )

                    preview.surfaceProvider = this.surfaceProvider
                }
                previewView
            }
        )

        // Bounding Box Overlay
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = if (isScanComplete) R.drawable.bounding_box_scanned else R.drawable.bounding_box_for_scan),
                contentDescription = "Bounding Box",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(50.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 25.dp)
        ) {
            IconButton(
                onClick = {
                    navigateBack()
                }, modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back Arrow",
                )
            }

            if (isScanComplete) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Icon(
                        painter = painterResource(id = R.drawable.coin),
                        contentDescription = "rewarded",
                        tint = Color(0xFFFFD700)
                    )
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun ScanPreview() {
    ScanComposable(viewModel = MainViewModel(), navigateBack = {}, userId = "12345")
}