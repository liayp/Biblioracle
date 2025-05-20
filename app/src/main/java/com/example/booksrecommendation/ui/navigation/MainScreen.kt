package com.example.booksrecommendation.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.booksrecommendation.R
import com.example.booksrecommendation.ui.screens.favorite.FavoriteScreen
import com.example.booksrecommendation.ui.screens.detail.DetailScreen
import com.example.booksrecommendation.ui.screens.detail.DetailViewModel
import com.example.booksrecommendation.ui.screens.favorite.FavoriteViewModel
import com.example.booksrecommendation.ui.screens.home.HomeScreen
import com.example.booksrecommendation.ui.screens.home.HomeViewModel
import com.example.booksrecommendation.ui.screens.home.category.CategoryScreen
import com.example.booksrecommendation.ui.screens.home.search.SearchScreen
import com.example.booksrecommendation.ui.screens.scanner.ResultScreen
import com.example.booksrecommendation.ui.screens.scanner.ScannerScreen

const val SCANNER_ROUTE = "scanner"

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = remember { BottomNavItem.routes() }
    val showBottomBar = currentRoute in bottomBarRoutes
    val showFab = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomAppBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                ) {
                    BottomNavigationBar(navController = navController)
                }
            }
        },
        floatingActionButton = {
            if (showFab) {
                val gradientBrush = Brush.linearGradient(
                    colors = listOf(Color(0xFF9C27B0), Color(0xFFE91E63))
                )
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .offset(y = 48.dp)
                        .background(brush = gradientBrush, shape = CircleShape)
                        .clickable { navController.navigateSingleTopTo(SCANNER_ROUTE) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_book_24),
                        contentDescription = "scanner",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(SCANNER_ROUTE) {
                val homeViewModel: HomeViewModel = hiltViewModel()
                ScannerScreen(navController = navController, homeViewModel = homeViewModel)
            }
            composable(BottomNavItem.Favorite.route) {
                val favViewModel: FavoriteViewModel = hiltViewModel()
                FavoriteScreen(favViewModel = favViewModel, navController = navController)
            }
            composable(
                route = "detail/{bookId}",
                arguments = listOf(navArgument("bookId") { defaultValue = "" })
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                val detailViewModel: DetailViewModel = hiltViewModel()
                val favViewModel: FavoriteViewModel = hiltViewModel()
                DetailScreen(bookId = bookId, viewModel = detailViewModel, navController = navController, favViewModel = favViewModel)
            }
            composable(
                route = "category/{categoryName}",
                arguments = listOf(navArgument("categoryName") { defaultValue = "" })
            ) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                val homeViewModel: HomeViewModel = hiltViewModel()
                val favViewModel: FavoriteViewModel = hiltViewModel()
                CategoryScreen(categoryName = categoryName, viewModel = homeViewModel, navController = navController, favViewModel = favViewModel)
            }
            composable(
                route = "search/{query}",
                arguments = listOf(navArgument("query") { defaultValue = "" })
            ) { backStackEntry ->
                val query = backStackEntry.arguments?.getString("query") ?: ""
                val homeViewModel: HomeViewModel = hiltViewModel()
                val favViewModel: FavoriteViewModel = hiltViewModel()
                SearchScreen(query = query, viewModel = homeViewModel, navController = navController, favViewModel = favViewModel)
            }
            composable("result/{scannedText}") { backStackEntry ->
                val scannedText = backStackEntry.arguments?.getString("scannedText") ?: ""
                ResultScreen(text = scannedText, navController = navController)
            }

        }
    }
}
