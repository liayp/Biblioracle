package com.example.booksrecommendation.data.repository

import com.example.booksrecommendation.data.retrofit.ApiService
import com.example.booksrecommendation.data.response.BookListResponse
import com.example.booksrecommendation.data.response.ItemsItem
import retrofit2.Response
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getBooks(query: String): Response<BookListResponse> {
        return apiService.getBooks(query)
    }

    suspend fun getBookById(id: String): Response<ItemsItem> {
        return apiService.getBookById(id)
    }

}
