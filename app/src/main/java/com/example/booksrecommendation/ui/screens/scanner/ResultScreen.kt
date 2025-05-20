package com.example.booksrecommendation.ui.screens.scanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.booksrecommendation.R
import com.example.booksrecommendation.ui.navigation.TopAppBarWithBackIcon

@Composable
fun ResultScreen(text: String, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(
                title = stringResource(R.string.title_detail),
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text)
        }
    }
}
