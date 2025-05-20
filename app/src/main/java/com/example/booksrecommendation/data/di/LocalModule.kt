package com.example.booksrecommendation.data.di

import android.content.Context
import androidx.room.Room
import com.example.booksrecommendation.data.local.FavoriteBookDao
import com.example.booksrecommendation.data.local.FavoriteBookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): FavoriteBookDatabase {
        return Room.databaseBuilder(
            appContext,
            FavoriteBookDatabase::class.java,
            "favorite_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFavoriteBookDao(database: FavoriteBookDatabase): FavoriteBookDao {
        return database.favoriteBookDao()
    }
}
