package com.example.booksrecommendation.data.repository

import com.example.booksrecommendation.data.local.FavoriteBookDao
import com.example.booksrecommendation.data.local.FavoriteBookEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteBookRepository @Inject constructor(
    private val dao: FavoriteBookDao
) {
    fun getAllFavorites(): Flow<List<FavoriteBookEntity>> = dao.getAllFavorites()

    fun isBookFavorited(bookId: String): Flow<Boolean> = dao.isFavorite(bookId)

    suspend fun addFavorite(book: FavoriteBookEntity) = dao.insertFavorite(book)

    suspend fun removeFavorite(book: FavoriteBookEntity) = dao.deleteFavorite(book)

    suspend fun removeFavoriteById(bookId: String) = dao.deleteFavoriteById(bookId)

    suspend fun isBookFavoritedSync(bookId: String): Boolean {
        return dao.isBookFavoritedSync(bookId)
    }

}
