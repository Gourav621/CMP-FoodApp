package com.example.cmpproject.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    val categories: List<Category>
)

@Serializable
data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)
