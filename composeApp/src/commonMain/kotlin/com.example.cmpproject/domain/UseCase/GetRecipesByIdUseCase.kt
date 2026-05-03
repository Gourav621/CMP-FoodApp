package com.example.cmpproject.domain.UseCase

import com.example.cmpproject.domain.Recipes
import com.example.cmpproject.model.Meal
import com.example.cmpproject.model.MealX

class GetRecipesByIdUseCase(private val repo: Recipes) {
    suspend fun  invoke(id: Int): MealX{
        return repo.getRecipeById(id = id)
    }
}