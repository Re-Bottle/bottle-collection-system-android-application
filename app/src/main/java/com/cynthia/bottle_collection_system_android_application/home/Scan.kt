package com.cynthia.bottle_collection_system_android_application.home

import android.Manifest
import android.content.Intent
import android.provider.Settings
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
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    // Check if camera permission is granted
    val isCameraPermissionGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

    LaunchedEffect(Unit) {
        /*
        * If camera permission is not granted, redirect the user to app settings
        * Show a toast message prompting the user to grant the permission
        * Else, Open app settings so the user can manually grant the camera permission
        * */
        if (!isCameraPermissionGranted) {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()

            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = android.net.Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        } else {
            viewModel.resetScanState()
        }
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

                                            // Handle the scanned barcode
                                            viewModel.claimScan(
                                                userId = userId,
                                                scanData = displayValue,
                                                onError = { errorMessage ->
                                                    println("Error while claiming: $errorMessage")
                                                    Toast.makeText(
                                                        context,
                                                        "Error: $errorMessage",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                },
                                                onSuccess = { message->
                                                    Log.d("Claim Scan", "ScanComposable: $message")
                                                })
                                        }
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Error: ${it.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
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