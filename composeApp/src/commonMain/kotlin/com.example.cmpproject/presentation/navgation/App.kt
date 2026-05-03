package com.example.cmpproject

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.cmpproject.presentation.CartScreen
import com.example.cmpproject.presentation.CategoryScreen
import com.example.cmpproject.presentation.CheckoutScreen
import com.example.cmpproject.presentation.DetailScreen
import com.example.cmpproject.presentation.FavoritesScreen
import com.example.cmpproject.presentation.HomeScreen
import com.example.cmpproject.presentation.OrderSuccessScreen
import com.example.cmpproject.presentation.ProfileScreen
import com.example.cmpproject.presentation.SearchScreen
import com.example.cmpproject.presentation.navgation.Routes

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(navController, startDestination = Routes.Home) {
            composable<Routes.Home> {
                HomeScreen(navController)
            }

            composable<Routes.Search> {
                SearchScreen(navController)
            }

            composable<Routes.Cart> {
                CartScreen(navController)
            }

            composable<Routes.Favorites> {
                FavoritesScreen(navController)
            }

            composable<Routes.Profile> {
                ProfileScreen(navController)
            }

            composable<Routes.Details> { backStackEntry ->
                val details: Routes.Details = backStackEntry.toRoute()
                DetailScreen(details.id, navController)
            }

            composable<Routes.Checkout> { backStackEntry ->
                val checkout: Routes.Checkout = backStackEntry.toRoute()
                CheckoutScreen(navController)
            }

            composable<Routes.OrderSuccess> {
                OrderSuccessScreen(navController)
            }

            composable<Routes.Category> { backStackEntry ->
                val category: Routes.Category = backStackEntry.toRoute()
                CategoryScreen(category.categoryName, navController)
            }
        }
    }
}
