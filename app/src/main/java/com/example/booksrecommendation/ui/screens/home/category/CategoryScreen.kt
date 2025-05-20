package com.example.booksrecommendation.ui.screens.home.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.booksrecommendation.R
import com.example.booksrecommendation.data.helper.toFavoriteEntity
import com.example.booksrecommendation.ui.navigation.TopAppBarWithBackIcon
import com.example.booksrecommendation.ui.screens.BookCard
import com.example.booksrecommendation.ui.screens.favorite.FavoriteViewModel
import com.example.booksrecommendation.ui.screens.home.HomeViewModel

@Composable
fun CategoryScreen(
    categoryName: String,
    viewModel: HomeViewModel,
    navController: NavController,
    favViewModel: FavoriteViewModel
) {
    val booksResponse = viewModel.booksResponse.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val favorites = favViewModel.favoriteBooks.observeAsState().value ?: emptyList()

    LaunchedEffect(categoryName) {
        viewModel.getBooks(categoryName)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBarWithBackIcon(
            title = "$categoryName " +  stringResource(R.string.title_category),
            onBackClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        )

        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
            errorMessage != null -> {
                Text(
                    text = "Error: $errorMessage",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
            else -> {
                booksResponse?.items?.let { items ->
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items) { book ->
                            if (book != null) {
                                BookCard(
                                    book = book,
                                    isFavorite = favorites.any { it.id == book.id },
                                    onFavoriteToggle = { selectedBook, _ ->
                                        favViewModel.toggleFavorite(selectedBook.toFavoriteEntity())
                                    },
                                    onClick = { navController.navigate("detail/${book.id}") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
