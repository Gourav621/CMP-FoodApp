package com.example.cmpproject.domain.UseCase

import com.example.cmpproject.CartItem
import com.example.cmpproject.domain.Recipes
import com.example.cmpproject.model.Meal
import kotlinx.coroutines.flow.Flow

class GetCartItemUseCase(private val repo: Recipes) {
    suspend fun getCartItems(): Flow<List<CartItem>> {
        return repo.getCartItems()
    }
    suspend fun addCart(recipe: Meal) {
        repo.addToCart(recipe)
    }
    suspend  fun remove(recipeId: Int) {
        repo.removeFromCart(recipeId)
    }
    suspend fun updateQuantity(idMeal: String, quantity: Int) {
        repo.updateQuantity(idMeal, quantity)
    }
    suspend  fun clear() {
        repo.clearCart()
    }
}


