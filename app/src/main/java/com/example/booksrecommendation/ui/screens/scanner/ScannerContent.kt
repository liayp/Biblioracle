package com.example.booksrecommendation.ui.screens.scanner

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture


@Composable
fun ScannerContent(
    hasCameraPermission: State<Boolean>,
    showCapturedImage: MutableState<Boolean>,
    capturedBitmap: MutableState<Bitmap?>,
    selectedGalleryBitmap: MutableState<Bitmap?>,
    isGalleryImageShown: MutableState<Boolean>,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    lifecycleOwner: LifecycleOwner,
    imageCapture: ImageCapture,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (hasCameraPermission.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)
                    .padding(horizontal = 20.dp, vertical = 45.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                when {
                    showCapturedImage.value && capturedBitmap.value != null -> {
                        // Kamera
                        Image(
                            bitmap = capturedBitmap.value!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(25.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    isGalleryImageShown.value && selectedGalleryBitmap.value != null -> {
                        // Galeri
                        Image(
                            bitmap = selectedGalleryBitmap.value!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(25.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        // Kamera live preview
                        AndroidView(factory = { ctx ->
                            val previewView = PreviewView(ctx)
                            cameraProviderFuture.addListener({
                                val cameraProvider = cameraProviderFuture.get()
                                val preview = androidx.camera.core.Preview.Builder().build().also {
                                    it.surfaceProvider = previewView.surfaceProvider
                                }
                                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageCapture
                                )
                            }, ContextCompat.getMainExecutor(ctx))
                            previewView
                        }, modifier = Modifier.fillMaxSize())
                    }
                }

                if (showCapturedImage.value || isGalleryImageShown.value) {
                    IconButton(
                        onClick = {
                            showCapturedImage.value = false
                            isGalleryImageShown.value = false
                            capturedBitmap.value = null
                            selectedGalleryBitmap.value = null
                        },
                        modifier = Modifier.align(Alignment.TopEnd).padding(13.dp).size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color.White
                        )
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Camera permission is required", color = Color.White)
            }
        }
    }
}
