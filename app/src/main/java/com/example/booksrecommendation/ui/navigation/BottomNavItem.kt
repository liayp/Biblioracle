package com.example.booksrecommendation.ui.navigation

import androidx.annotation.DrawableRes
import com.example.booksrecommendation.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val iconRes: Int,
    val label: String
) {
    data object Home : BottomNavItem("home", R.drawable.baseline_home_24, "Home")
    data object Favorite : BottomNavItem("favorite", R.drawable.baseline_favorite_24, "Favorite")

    companion object {
        val items = listOf(Home, Favorite)
        fun routes() = items.map { it.route }
    }
}
