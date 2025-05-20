package com.example.booksrecommendation.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.booksrecommendation.R
import com.example.booksrecommendation.data.helper.toFavoriteEntity
import com.example.booksrecommendation.data.local.FavoriteBookEntity
import com.example.booksrecommendation.data.response.ItemsItem
import com.example.booksrecommendation.ui.navigation.TopAppBarWithBackIcon
import com.example.booksrecommendation.ui.screens.BookCard
import com.example.booksrecommendation.ui.screens.favorite.FavoriteViewModel

@Composable
fun DetailScreen(
    bookId: String,
    viewModel: DetailViewModel,
    favViewModel: FavoriteViewModel,
    navController: NavController
) {
    val bookState = viewModel.book.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()
    val relatedByCategory = viewModel.relatedByCategory.collectAsState()
    val relatedByAuthor = viewModel.relatedByAuthor.collectAsState()
    val isFavorite by favViewModel.isFavorited(bookId).observeAsState(initial = false)
    val favorites by favViewModel.favoriteBooks.observeAsState(initial = emptyList())

    LaunchedEffect(bookId) {
        viewModel.loadBookDetails(bookId)
    }

    when {
        isLoading.value -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        bookState.value != null -> {
            val book = bookState.value!!
            val volumeInfo = book.volumeInfo
            val imageUrl = volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://")
            val title = volumeInfo?.title ?: "No Title"
            val authors = volumeInfo?.authors?.joinToString(", ") ?: "Unknown Author"
            val publisher = volumeInfo?.publisher ?: "No Publisher"
            val publishedDate = volumeInfo?.publishedDate ?: "No Published Date"
            val rawDescription = volumeInfo?.description ?: "No Description"
            val description = HtmlCompat.fromHtml(rawDescription, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

            Scaffold(
                topBar = {
                    TopAppBarWithBackIcon(
                        title = stringResource(R.string.title_detail),
                        onBackClick = { navController.popBackStack() }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 180.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(145.dp))

                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )

                            Text(
                                text = "by $authors",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )

                            Text(
                                text = "Publisher: $publisher",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            Text(
                                text = "Published Date: $publishedDate",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            Text(
                                text = "Description:",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 5.dp)
                            )

                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )

                            RelatedBooksSection("More in Genre", relatedByCategory.value, navController, favorites, favViewModel)
                            RelatedBooksSection("More by Author", relatedByAuthor.value, navController, favorites, favViewModel)
                        }

                        // Thumbnail + Favorite FAB
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = 40.dp)
                        ) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                modifier = Modifier
                                    .width(180.dp)
                                    .height(270.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = imageUrl),
                                    contentDescription = title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            IconButton(
                                onClick = {
                                    val favoriteBook = book.toFavoriteEntity()
                                    favViewModel.toggleFavorite(favoriteBook)
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 12.dp, y = (-12).dp)
                                    .background(
                                        color = Color.White.copy(alpha = 0.85f),
                                        shape = RoundedCornerShape(50)
                                    )
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) Color.Red else Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        errorMessage.value != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${errorMessage.value}")
            }
        }
    }
}

@Composable
fun RelatedBooksSection(
    title: String,
    books: List<ItemsItem>,
    navController: NavController,
    favorites: List<FavoriteBookEntity>,
    favViewModel: FavoriteViewModel
) {
    if (books.isNotEmpty()) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(books.size) { index ->
                    val relatedItem = books[index]
                    Box(
                        modifier = Modifier.width(140.dp)
                    ) {
                        BookCard(
                            book = relatedItem,
                            isFavorite = favorites.any { it.id == relatedItem.id },
                            onFavoriteToggle = { book, _ ->
                                favViewModel.toggleFavorite(book.toFavoriteEntity())
                            },
                            onClick = { navController.navigate("detail/${relatedItem.id}") }
                        )
                    }
                }
            }
        }
    }
}