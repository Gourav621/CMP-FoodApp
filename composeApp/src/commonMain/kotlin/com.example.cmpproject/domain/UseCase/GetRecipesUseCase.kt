package com.example.cmpproject.domain.UseCase

import com.example.cmpproject.domain.Recipes
import com.example.cmpproject.model.MealDetailResponse
import com.example.cmpproject.model.MealResponse
import com.example.cmpproject.model.CategoryResponse

class GetRecipesUseCase ( private val repo: Recipes) {
   suspend fun invoke(): MealResponse {
        return repo.getRecipes()
    }
    
    suspend fun getCategories(): CategoryResponse {
        return repo.getCategories()
    }

    suspend fun searchRecipes(query: String): MealResponse {
        return repo.searchRecipes(query)
    }

   suspend fun getRecipesByCategory(category: String): MealResponse {
        return repo.getRecipesByCategory(category)
    }
}
