package com.example.booksrecommendation.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(book: FavoriteBookEntity)

    @Delete
    suspend fun deleteFavorite(book: FavoriteBookEntity)

    @Query("DELETE FROM favorite_books WHERE id = :bookId")
    suspend fun deleteFavoriteById(bookId: String)

    @Query("SELECT * FROM favorite_books ORDER BY title ASC")
    fun getAllFavorites(): Flow<List<FavoriteBookEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_books WHERE id = :bookId)")
    fun isFavorite(bookId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_books WHERE id = :bookId)")
    suspend fun isBookFavoritedSync(bookId: String): Boolean

}
