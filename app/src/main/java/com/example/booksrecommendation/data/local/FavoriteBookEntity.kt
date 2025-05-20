package com.example.booksrecommendation.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite_books")
data class FavoriteBookEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "authors")
    val authors: String,

    @ColumnInfo(name = "publisher")
    val publisher: String,

    @ColumnInfo(name = "publishedDate")
    val publishedDate: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "thumbnail")
    val thumbnail: String
) : Parcelable
