package com.example.cmpproject.model

import kotlinx.serialization.Serializable


@Serializable
data class MealDetailResponse(
    val meals: List<MealX>
)