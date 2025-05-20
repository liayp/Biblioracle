package com.example.booksrecommendation.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrecommendation.data.repository.BookRepository
import com.example.booksrecommendation.data.response.ItemsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val _book = MutableStateFlow<ItemsItem?>(null)
    val book: StateFlow<ItemsItem?> = _book.asStateFlow()

    private val _relatedByCategory = MutableStateFlow<List<ItemsItem>>(emptyList())
    val relatedByCategory = _relatedByCategory.asStateFlow()

    private val _relatedByAuthor = MutableStateFlow<List<ItemsItem>>(emptyList())
    val relatedByAuthor = _relatedByAuthor.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun loadBookDetails(bookId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getBookById(bookId)
                if (response.isSuccessful) {
                    val bookItem = response.body()
                    _book.value = bookItem

                    val category = bookItem?.volumeInfo?.categories?.firstOrNull()
                    val author = bookItem?.volumeInfo?.authors?.firstOrNull()

                    if (category != null) loadRelatedBooksByCategory(category)
                    if (author != null) loadRelatedBooksByAuthor(author)
                } else {
                    _errorMessage.value = "Failed to load book details"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadRelatedBooksByCategory(category: String) {
        viewModelScope.launch {
            try {
                val response = repository.getBooks("subject:$category")
                if (response.isSuccessful) {
                    _relatedByCategory.value = response.body()?.items?.filterNotNull() ?: emptyList()
                }
            } catch (_: Exception) { }
        }
    }

    private fun loadRelatedBooksByAuthor(author: String) {
        viewModelScope.launch {
            try {
                val response = repository.getBooks("inauthor: $author")
                if (response.isSuccessful) {
                    _relatedByAuthor.value = response.body()?.items?.filterNotNull() ?: emptyList()
                }
            } catch (_: Exception) { }
        }
    }
}
