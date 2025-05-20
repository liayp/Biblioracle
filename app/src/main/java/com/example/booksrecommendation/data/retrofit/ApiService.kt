package com.example.booksrecommendation.data.retrofit

import com.example.booksrecommendation.data.response.BookListResponse
import com.example.booksrecommendation.data.response.ItemsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String
    ): Response<BookListResponse>

    @GET("volumes/{id}")
    suspend fun getBookById(
        @Path("id") id: String
    ): Response<ItemsItem>

}
