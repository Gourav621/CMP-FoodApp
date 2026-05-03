package com.example.cmpproject.model

import kotlinx.serialization.Serializable


@Serializable
data class MealResponse(
    val meals: List<Meal>
)