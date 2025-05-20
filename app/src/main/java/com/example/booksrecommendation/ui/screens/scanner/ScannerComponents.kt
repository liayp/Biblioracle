package com.example.booksrecommendation.ui.screens.scanner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.booksrecommendation.R
import com.example.booksrecommendation.ui.navigation.TopAppBarWithBackIcon
import java.util.concurrent.ExecutorService


@Composable
fun ScannerTopBar(
    navController: NavController,
    cameraExecutor: ExecutorService
) {
    TopAppBarWithBackIcon(
        title = stringResource(R.string.title_scanner),
        onBackClick = {
            cameraExecutor.shutdown()
            navController.navigate("home") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    )
}

@Composable
fun ScannerBottomBar(
    onGalleryClick: () -> Unit
) {
    BottomAppBar(tonalElevation = 4.dp, containerColor = Color.White) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconTextColumn(
                iconId = R.drawable.baseline_photo_camera_24,
                text = stringResource(R.string.title_camera),
                tint = Color(0xFFBA68C8),
                onClick = { /* Kamera */ }
            )
            IconTextColumn(
                iconId = R.drawable.baseline_photo_library_24,
                text = stringResource(R.string.title_gallery),
                tint = Color(0xFFB0BEC5),
                onClick = onGalleryClick
            )

        }
    }
}

@Composable
fun IconTextColumn(iconId: Int, text: String, tint: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() })
    {
        Icon(painter = painterResource(id = iconId), contentDescription = text, tint = tint, modifier = Modifier.size(20.dp))
        Text(text = text, color = tint, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}
