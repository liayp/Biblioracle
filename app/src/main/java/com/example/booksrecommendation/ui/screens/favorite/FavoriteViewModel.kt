package com.example.booksrecommendation.ui.screens.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.booksrecommendation.data.local.FavoriteBookEntity
import com.example.booksrecommendation.data.repository.FavoriteBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: FavoriteBookRepository
) : ViewModel() {

    val favoriteBooks: LiveData<List<FavoriteBookEntity>> =
        repository.getAllFavorites().asLiveData()

    fun toggleFavorite(book: FavoriteBookEntity) {
        viewModelScope.launch {
            val isFav = repository.isBookFavoritedSync(book.id)
            if (isFav) {
                repository.removeFavoriteById(book.id)
            } else {
                repository.addFavorite(book)
            }
        }
    }

    fun removeFavoriteById(bookId: String) {
        viewModelScope.launch {
            repository.removeFavoriteById(bookId)
        }
    }

    fun isFavorited(bookId: String): LiveData<Boolean> =
        repository.isBookFavorited(bookId).asLiveData()
}

