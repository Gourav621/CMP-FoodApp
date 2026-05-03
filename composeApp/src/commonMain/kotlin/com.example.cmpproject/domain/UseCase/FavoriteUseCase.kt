package com.example.cmpproject.domain.UseCase

import com.example.cmpproject.domain.Recipes
import com.example.cmpproject.model.Meal
import kotlinx.coroutines.flow.Flow

class FavoriteUseCase(private val repo: Recipes) {
    fun getFavorites(): Flow<List<Meal>> {
        return repo.getFavorites()
    }

    suspend fun insertFavorite(meal: Meal) {
        repo.insertFavorite(meal)
    }

    suspend fun deleteFavorite(idMeal: String) {
        repo.deleteFavorite(idMeal)
    }

    fun isFavorite(idMeal: String): Flow<Boolean> {
        return repo.isFavorite(idMeal)
    }
}
