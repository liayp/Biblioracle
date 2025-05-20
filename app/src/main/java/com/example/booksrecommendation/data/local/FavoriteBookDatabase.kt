package com.example.booksrecommendation.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteBookEntity::class], version = 1, exportSchema = false)
abstract class FavoriteBookDatabase : RoomDatabase() {
    abstract fun favoriteBookDao(): FavoriteBookDao
}
