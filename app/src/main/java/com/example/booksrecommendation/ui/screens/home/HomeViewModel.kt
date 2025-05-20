package com.example.booksrecommendation.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrecommendation.data.repository.BookRepository
import com.example.booksrecommendation.data.response.BookListResponse
import com.example.booksrecommendation.data.response.ItemsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel()
{
    var booksResponse = mutableStateOf<BookListResponse?>(null)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var isLoading = mutableStateOf(false)
        private set

    fun getBooks(query: String) {
        isLoading.value = true
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                bookRepository.getBooks(query)
            }

            if (response.isSuccessful) {
                val books = response.body()?.items
                val sortedBooks = books?.sortedByDescending { item ->
                    item?.volumeInfo?.publishedDate?.let { parseDate(it) } ?: Date(0)
                }

                booksResponse.value = BookListResponse(
                    items = sortedBooks ?: emptyList()
                )
            } else {
                errorMessage.value = "Error: ${response.message()}"
            }
            isLoading.value = false
        }
    }

    fun searchBooksByTitleOrAuthor(keyword: String) {
        val encodedKeyword = keyword.trim().replace(" ", "+")
        val query = "(intitle:$encodedKeyword)+OR+(inauthor:$encodedKeyword)"
        getBooks(query)
    }

    fun getFirstBookFromScan(text: String, onResult: (ItemsItem?) -> Unit) {
        viewModelScope.launch {
            val query = text.trim().replace(" ", "+")
            val response = withContext(Dispatchers.IO) {
                bookRepository.getBooks(query)
            }

            if (response.isSuccessful) {
                val book = response.body()?.items?.firstOrNull()
                onResult(book)
            } else {
                onResult(null)
            }
        }
    }


    private fun parseDate(dateStr: String): Date {
        val formats = listOf("yyyy-MM-dd", "yyyy-MM", "yyyy")
        for (format in formats) {
            try {
                return SimpleDateFormat(format, Locale.getDefault()).parse(dateStr)!!
            } catch (_: Exception) {}
        }
        return Date(0)
    }

}
