package com.example.cmpproject.data

import com.example.cmpproject.CartItem
import com.example.cmpproject.common.BASE_URL
import com.example.cmpproject.domain.KtorClient
import com.example.cmpproject.domain.Recipes
import com.example.cmpproject.model.LocationModel
import com.example.cmpproject.model.Meal
import com.example.cmpproject.model.MealDetailResponse
import com.example.cmpproject.model.MealX
import com.example.cmpproject.model.MealResponse
import com.example.cmpproject.model.CategoryResponse
import com.example.cmpproject.data.local.SqlDelightDataSource
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepoImpl(
    private val httpClient: KtorClient,
    private val sqlDelightDataSource: SqlDelightDataSource
) : Recipes {

    override fun getCartItems(): Flow<List<CartItem>> {
        return sqlDelightDataSource.getCartItems().map { list ->
            list.map {
                CartItem(
                    recipe = Meal(
                        idMeal = it.idMeal,
                        strMeal = it.mealName,
                        strMealThumb = it.mealThumb
                    ),
                    quantity = it.quantity.toInt()
                )
            }
        }
    }

    override suspend fun getRecipes(): MealResponse {
        return httpClient.client.get("$BASE_URL/filter.php?c=Seafood")
            .body<MealResponse>()
    }

    override suspend fun getRecipeById(id: Int): MealX {
        val response = httpClient.client
            .get("$BASE_URL/lookup.php?i=$id")
            .body<MealDetailResponse>()

        return response.meals.first()
    }

    override suspend fun getCategories(): CategoryResponse {
        return httpClient.client
            .get("$BASE_URL/categories.php")
            .body<CategoryResponse>()
    }

    override suspend fun searchRecipes(query: String): MealResponse {
        return httpClient.client
            .get("$BASE_URL/search.php?s=$query")
            .body<MealResponse>()
    }

    override suspend fun getRecipesByCategory(category: String): MealResponse {
        return httpClient.client
            .get("$BASE_URL/filter.php?c=$category")
            .body<MealResponse>()
    }

    override suspend fun getCurrentLocation(): LocationModel {
        return LocationModel(
            latitude = 28.9845,
            longitude = 77.70606,
            address = "Merrut,Uttar Pradesh,India"
        )
    }

    override suspend fun saveLocation(latitude: Double, longitude: Double) {
        // TODO
    }

    override suspend fun addToCart(recipe: Meal) {
        sqlDelightDataSource.insertCartItem(
            idMeal = recipe.idMeal,
            mealName = recipe.strMeal,
            mealThumb = recipe.strMealThumb,
            quantity = 1
        )
    }

    override suspend fun removeFromCart(recipeId: Int) {
        sqlDelightDataSource.deleteCartItem(recipeId.toString())
    }

    override suspend fun clearCart() {
        sqlDelightDataSource.clearCart()
    }

    override suspend fun updateQuantity(idMeal: String, quantity: Int) {
        sqlDelightDataSource.updateQuantity(idMeal, quantity.toLong())
    }

    override fun getFavorites(): Flow<List<Meal>> {
        return sqlDelightDataSource.getFavorites().map { list ->
            list.map {
                Meal(
                    idMeal = it.idMeal,
                    strMeal = it.mealName,
                    strMealThumb = it.mealThumb
                )
            }
        }
    }

    override suspend fun insertFavorite(meal: Meal) {
        sqlDelightDataSource.insertFavorite(
            idMeal = meal.idMeal,
            mealName = meal.strMeal,
            mealThumb = meal.strMealThumb
        )
    }

    override suspend fun deleteFavorite(idMeal: String) {
        sqlDelightDataSource.deleteFavorite(idMeal)
    }

    override fun isFavorite(idMeal: String): Flow<Boolean> {
        return sqlDelightDataSource.isFavorite(idMeal)
    }

    override suspend fun getCategory(): MealDetailResponse {
        throw UnsupportedOperationException("Use getCategories() instead")
    }
}
