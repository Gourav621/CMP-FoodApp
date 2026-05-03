package com.example.cmpproject.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cmpproject.presentation.navgation.Routes

data class BottomTabItem(
    val tab: Routes,
    val label: String,
    val icon: ImageVector
)

val defaultHomeTabs = listOf(
    BottomTabItem(Routes.Home, "Home", Icons.Outlined.Home),
    BottomTabItem(Routes.Search, "Explore", Icons.Outlined.Explore),
    BottomTabItem(Routes.Favorites, "Reorder", Icons.Outlined.FavoriteBorder),
    BottomTabItem(Routes.Profile, "Account", Icons.Outlined.Person),
)

@Composable
fun BottomBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        NavigationBar(
            containerColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // ✅ ONLY THIS
        ) {
            defaultHomeTabs.forEach { item ->

                val isSelected =
                    currentRoute?.contains(item.tab::class.simpleName ?: "") == true

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.tab) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp) // ✅ standard size
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFFC8019),
                        selectedTextColor = Color(0xFFFC8019),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color(0xFFFFF3E0)
                    )
                )
            }
        }
    }
}
