package com.example.booksrecommendation.ui.screens.scanner

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.booksrecommendation.R
import com.example.booksrecommendation.ui.screens.home.HomeViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService

@Composable
fun ScannerFab(
    showCapturedImage: MutableState<Boolean>,
    showScanFab: MutableState<Boolean>,
    imageCapture: ImageCapture,
    isGalleryImageShown: MutableState<Boolean>,
    cameraExecutor: ExecutorService,
    capturedBitmap: MutableState<Bitmap?>,
    selectedGalleryBitmap: MutableState<Bitmap?>,
    navController: NavController,
    context: Context,
    homeViewModel: HomeViewModel
) {
    if (!showCapturedImage.value && !isGalleryImageShown.value) {
        FloatingActionButton(
            onClick = {
                imageCapture.takePicture(
                    cameraExecutor,
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(imageProxy: ImageProxy) {
                            val bitmap = imageProxy.toBitmap().fixRotation(imageProxy.imageInfo.rotationDegrees)
                            capturedBitmap.value = bitmap
                            showCapturedImage.value = true
                            showScanFab.value = true
                            imageProxy.close()
                        }
                        override fun onError(exception: ImageCaptureException) {
                            exception.printStackTrace()
                        }
                    }
                )
            },
            containerColor = Color(0xFFE91E63),
            shape = CircleShape,
            modifier = Modifier.size(56.dp).offset(y = (-20).dp)
        ) {
            Icon(painterResource(id = R.drawable.baseline_photo_camera_24), contentDescription = "Camera", tint = Color.White, modifier = Modifier.size(28.dp))
        }
    } else if (showScanFab.value) {
        FloatingActionButton(
            onClick = {
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

                val bitmap = if (showCapturedImage.value) {
                    capturedBitmap.value
                } else if (isGalleryImageShown.value) {
                    capturedBitmap.value ?: selectedGalleryBitmap.value
                } else null

                if (bitmap == null) {
                    Toast.makeText(context, "Gambar tidak tersedia", Toast.LENGTH_SHORT).show()
                    return@FloatingActionButton
                }

                val inputImage = InputImage.fromBitmap(bitmap, 0)

                recognizer.process(inputImage)
                    .addOnSuccessListener { visionText ->
                        val scannedText = visionText.text
                        homeViewModel.getFirstBookFromScan(scannedText) { book ->
                            if (book != null) {
                                val bookId = book.id
                                navController.navigate("detail/$bookId")
                            } else {
                                Toast.makeText(context, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Gagal mengenali buku", Toast.LENGTH_SHORT).show()
                        it.printStackTrace()
                    }
            }
            ,
            containerColor = Color(0xFF9C27B0),
            shape = CircleShape,
            modifier = Modifier.size(56.dp).offset(y = (-20).dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Scan Text",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
