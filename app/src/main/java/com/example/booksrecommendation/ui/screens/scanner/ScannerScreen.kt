package com.example.booksrecommendation.ui.screens.scanner

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.booksrecommendation.ui.screens.home.HomeViewModel
import java.util.concurrent.Executors

@Composable
fun ScannerScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val showCapturedImage = remember { mutableStateOf(false) }
    val capturedBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val hasCameraPermission = remember { mutableStateOf(false) }
    val showScanFab = remember { mutableStateOf(false) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val imageCapture = remember { ImageCapture.Builder().build() }
    val selectedGalleryBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val isGalleryImageShown = remember { mutableStateOf(false) }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            selectedGalleryBitmap.value = bitmap
            isGalleryImageShown.value = true
            showScanFab.value = true
        }
    }

    SetupCameraPermission(context, hasCameraPermission)

    Scaffold(
        topBar = { ScannerTopBar(navController, cameraExecutor) },
        bottomBar = {
            ScannerBottomBar(
                onGalleryClick = {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            )
        },
                floatingActionButton = {
            ScannerFab(
                showCapturedImage = showCapturedImage,
                showScanFab = showScanFab,
                imageCapture = imageCapture,
                cameraExecutor = cameraExecutor,
                capturedBitmap = capturedBitmap,
                selectedGalleryBitmap = selectedGalleryBitmap,
                navController = navController,
                context = context,
                homeViewModel = homeViewModel,
                isGalleryImageShown = isGalleryImageShown
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        ScannerContent(
            hasCameraPermission = hasCameraPermission,
            showCapturedImage = showCapturedImage,
            capturedBitmap = capturedBitmap,
            selectedGalleryBitmap = selectedGalleryBitmap,
            isGalleryImageShown = isGalleryImageShown,
            cameraProviderFuture = cameraProviderFuture,
            lifecycleOwner = lifecycleOwner,
            imageCapture = imageCapture,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// Helper: Convert ImageProxy to Bitmap
fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

// Helper: Fix image rotation
fun Bitmap.fixRotation(rotationDegrees: Int): Bitmap {
    if (rotationDegrees == 0) return this
    val matrix = Matrix()
    matrix.postRotate(rotationDegrees.toFloat())
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}
