package com.example.cmpproject.presentation.navgation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Home : Routes()
@Serializable
data object Search : Routes()
    @Serializable
    data class Details(val id: Int) : Routes()
    @Serializable
    data object Cart : Routes()

    @Serializable
    data class Checkout(val id: Int) : Routes()
    @Serializable
    data object OrderSuccess : Routes()
    @Serializable
    data object Payment : Routes()


    @Serializable
    data object Favorites : Routes()
    @Serializable
    data object Profile : Routes()

    @Serializable
    data class Category(val categoryName: String) : Routes()


}