package com.example.booksrecommendation.ui.screens

import androidx.compose.material3.Card
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.booksrecommendation.data.response.ItemsItem
import com.example.booksrecommendation.ui.screens.favorite.FavoriteFab

@Composable
fun BookCard(
    book: ItemsItem,
    onClick: () -> Unit,
    isFavorite: Boolean,
    onFavoriteToggle: (ItemsItem, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.65f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val imageUrl = book.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://")
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = book.volumeInfo?.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                FavoriteFab(
                    isFavorite = isFavorite,
                    onToggle = { onFavoriteToggle(book, it) },
                    size = 36,
                    containerColor = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = book.volumeInfo?.title ?: "No Title",
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2
        )

        Text(
            text = book.volumeInfo?.authors?.joinToString(", ") ?: "Unknown Author",
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}
