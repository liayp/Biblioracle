@file:Suppress("NAME_SHADOWING")

package com.example.booksrecommendation.ui.screens.favorite

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.booksrecommendation.R
import com.example.booksrecommendation.data.helper.toFavoriteEntity
import com.example.booksrecommendation.data.helper.toItemsItem
import com.example.booksrecommendation.ui.navigation.TopAppBarWithBackIcon
import com.example.booksrecommendation.ui.screens.BookCard

@Composable
fun FavoriteScreen(
    favViewModel: FavoriteViewModel,
    navController: NavController
) {
    val favorites by favViewModel.favoriteBooks.observeAsState(emptyList())
    val books = favorites.map { it.toItemsItem() }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBarWithBackIcon(
            title = stringResource(R.string.title_favorite),
            showBackIcon = false
        )

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("You have no favorite books yet 😢")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(count = books.size) { index ->
                    val book = books[index]
                    BookCard(
                        book = book,
                        isFavorite = true,
                        onFavoriteToggle = { book, _ ->
                            favViewModel.toggleFavorite(book.toFavoriteEntity())
                        },
                        onClick = {
                            navController.navigate("detail/${book.id}")
                        }
                    )
                }
            }
        }
    }
}
