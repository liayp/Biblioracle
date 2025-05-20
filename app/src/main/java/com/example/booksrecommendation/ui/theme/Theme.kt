package com.example.booksrecommendation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = text,
    secondary = Teal200,
    surface = card,
    background = background
)

@Composable
fun BooksRecommendationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}
