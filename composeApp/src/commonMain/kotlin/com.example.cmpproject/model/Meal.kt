package com.example.cmpproject.model

import kotlinx.serialization.Serializable


@Serializable
data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,

    val favorite: Boolean = false,
    val price: Int = 0
)