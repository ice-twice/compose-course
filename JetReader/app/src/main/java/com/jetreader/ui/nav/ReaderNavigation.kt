package com.jetreader.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jetreader.ui.screens.details.SearchBookDetailsScreen
import com.jetreader.ui.screens.home.HomeScreen
import com.jetreader.ui.screens.login.LoginScreen
import com.jetreader.ui.screens.search.BookSearchScreen
import com.jetreader.ui.screens.splash.SplashScreen
import com.jetreader.ui.screens.update.UpdateAppBookScreen

@Composable
fun ReaderNavigation() {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreen.SplashScreen.name) {
        composable(ReaderScreen.SplashScreen.name) {
            SplashScreen(onNavigateNext = {
                navController.popBackStack(ReaderScreen.SplashScreen.name, inclusive = true)
                navController.navigate(it.name)
            })
        }
        composable(ReaderScreen.LoginScreen.name) {
            LoginScreen(navigateHome = {
                navController.popBackStack(ReaderScreen.LoginScreen.name, inclusive = true)
                navController.navigate(ReaderScreen.HomeScreen.name)
            })
        }
        composable(ReaderScreen.HomeScreen.name) {
            HomeScreen(
                onLogoutClick = {
                    navController.popBackStack(ReaderScreen.HomeScreen.name, inclusive = true)
                    navController.navigate(ReaderScreen.LoginScreen.name)
                },
                onBookFromReadingListClick = { bookId ->
                    navController.navigate("${ReaderScreen.UpdateAppBookScreen.name}/$bookId")
                },
                onAddNewBook = { navController.navigate(ReaderScreen.SearchBookScreen.name) },
                onBookFromReadingActivityClick = { bookId ->
                    navController.navigate("${ReaderScreen.UpdateAppBookScreen.name}/$bookId")
                }
            )
        }
        composable(ReaderScreen.SearchBookScreen.name) {
            BookSearchScreen(
                onBackArrowClick = { navController.popBackStack() },
                onBookClick = { bookId -> navController.navigate("${ReaderScreen.SearchBookDetailsScreen.name}/$bookId") }
            )
        }
        composable("${ReaderScreen.SearchBookDetailsScreen.name}/{bookId}") {
            val bookId: String = it.arguments?.getString("bookId") ?: ""
            SearchBookDetailsScreen(
                bookId = bookId,
                onBackArrowClick = { navController.popBackStack() },

                )
        }
        composable("${ReaderScreen.UpdateAppBookScreen.name}/{bookId}") {
            val bookId: String = it.arguments?.getString("bookId") ?: ""
            UpdateAppBookScreen(
                bookId = bookId,
                onBackArrowClick = { navController.popBackStack() },
                onBookClick = {
                    navController.navigate("${ReaderScreen.SearchBookDetailsScreen.name}/$bookId")
                },
            )
        }
    }
}
