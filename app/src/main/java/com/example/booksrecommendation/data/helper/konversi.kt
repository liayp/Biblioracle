package com.example.booksrecommendation.data.helper

import com.example.booksrecommendation.data.local.FavoriteBookEntity
import com.example.booksrecommendation.data.response.ImageLinks
import com.example.booksrecommendation.data.response.ItemsItem
import com.example.booksrecommendation.data.response.VolumeInfo

fun ItemsItem.toFavoriteEntity(): FavoriteBookEntity {
        return FavoriteBookEntity(
            id = this.id ?: "",
            title = this.volumeInfo?.title ?: "No Title",
            authors = this.volumeInfo?.authors?.joinToString(", ") ?: "Unknown Author",
            publisher = this.volumeInfo?.publisher ?: "-",
            publishedDate = this.volumeInfo?.publishedDate ?: "-",
            description = this.volumeInfo?.description ?: "-",
            thumbnail = this.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://") ?: ""
        )
    }

fun FavoriteBookEntity.toItemsItem(): ItemsItem {
    return ItemsItem(
        id = this.id,
        volumeInfo = VolumeInfo(
            title = this.title,
            authors = this.authors.split(", "),
            publisher = this.publisher,
            publishedDate = this.publishedDate,
            description = this.description,
            imageLinks = ImageLinks(
                thumbnail = this.thumbnail
            )
        )
    )
}
