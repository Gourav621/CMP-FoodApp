package com.example.cmpproject.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.example.cmpproject.example.Database
import com.example.cmpproject.example.Cart
import com.example.cmpproject.example.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SqlDelightDataSource(database: Database) {
    private val queries = database.cartQueries

    fun getCartItems(): Flow<List<Cart>> {
        return queries.getAllCartItems()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list ->
                // Ensure no duplicate IDs by grouping (just in case)
                list.groupBy { it.idMeal }.map { (_, group) -> group.first() }
            }
    }

    suspend fun insertCartItem(idMeal: String, mealName: String, mealThumb: String, quantity: Long) {
        queries.insertCartItem(idMeal, mealName, mealThumb, quantity)
    }

    suspend fun deleteCartItem(idMeal: String) {
        queries.deleteCartItem(idMeal)
    }

    suspend fun clearCart() {
        queries.clearCart()
    }

    suspend fun updateQuantity(idMeal: String, quantity: Long) {
        queries.updateQuantity(quantity, idMeal)
    }

    fun getFavorites(): Flow<List<Favorite>> {
        return queries.getAllFavorites()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    suspend fun insertFavorite(idMeal: String, mealName: String, mealThumb: String) {
        queries.insertFavorite(idMeal, mealName, mealThumb)
    }

    suspend fun deleteFavorite(idMeal: String) {
        queries.deleteFavorite(idMeal)
    }

    fun isFavorite(idMeal: String): Flow<Boolean> {
        return queries.isFavorite(idMeal)
            .asFlow()
            .mapToOne(Dispatchers.Default)
    }
}
