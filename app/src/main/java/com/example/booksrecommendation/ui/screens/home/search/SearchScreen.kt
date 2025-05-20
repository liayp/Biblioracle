package com.example.booksrecommendation.ui.screens.home.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.booksrecommendation.R
import com.example.booksrecommendation.data.helper.toFavoriteEntity
import com.example.booksrecommendation.ui.screens.BookCard
import com.example.booksrecommendation.ui.screens.favorite.FavoriteViewModel
import com.example.booksrecommendation.ui.screens.home.HomeViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    query: String,
    viewModel: HomeViewModel,
    favViewModel: FavoriteViewModel
) {
    val booksResponse = viewModel.booksResponse.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val searchQuery = remember { mutableStateOf("") }
    val favorites = favViewModel.favoriteBooks.observeAsState().value ?: emptyList()

    LaunchedEffect(query) {
        viewModel.getBooks(query)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                contentDescription = stringResource(R.string.title_camera),
                tint = Color(0xFFF8BBD0),
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                placeholder = { Text("Cari Judul Buku atau Penulis") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 44.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchQuery.value.isNotBlank()) {
                            navController.navigate("search/${searchQuery.value}")
                        }
                    }
                )
            )
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: $errorMessage",
                        color = Color.Red
                    )
                }
            }

            else -> {
                booksResponse?.items?.let { items ->
                    if (items.isNotEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(items) { book ->
                                book?.let {
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
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Books not found 😢")
                        }
                    }
                } ?: Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Books not found 😢")
                }
            }
        }
    }
}
