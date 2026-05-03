package com.example.cmpproject.domain

import com.example.cmpproject.CartItem
import com.example.cmpproject.model.LocationModel
import com.example.cmpproject.model.Meal
import com.example.cmpproject.model.MealDetailResponse
import com.example.cmpproject.model.MealX
import com.example.cmpproject.model.MealResponse
import com.example.cmpproject.model.CategoryResponse
import kotlinx.coroutines.flow.Flow

interface Recipes {
    suspend fun getRecipes(): MealResponse
    suspend fun getRecipeById(id: Int): MealX
    suspend fun getCategories(): CategoryResponse
    suspend fun searchRecipes(query: String): MealResponse
    suspend fun getRecipesByCategory(category: String): MealResponse
    suspend fun getCurrentLocation(): LocationModel
    suspend fun saveLocation(latitude: Double, longitude: Double)
    
    // Cart methods
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(recipe: Meal)
    suspend fun removeFromCart(recipeId: Int)
    suspend fun updateQuantity(idMeal: String, quantity: Int)
    suspend fun clearCart()

    // Favorite methods
    fun getFavorites(): Flow<List<Meal>>
    suspend fun insertFavorite(meal: Meal)
    suspend fun deleteFavorite(idMeal: String)
    fun isFavorite(idMeal: String): Flow<Boolean>

    // Legacy support
    suspend fun getCategory(): MealDetailResponse
}
