package com.example.cmpproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cmpproject.domain.UseCase.FavoriteUseCase
import com.example.cmpproject.domain.UseCase.GetCartItemUseCase
import com.example.cmpproject.domain.UseCase.GetRecipesByIdUseCase
import com.example.cmpproject.domain.UseCase.GetRecipesUseCase
import com.example.cmpproject.model.LocationModel
import com.example.cmpproject.model.Meal
import com.example.cmpproject.model.MealX
import com.example.cmpproject.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipesViewModel(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val getRecipesByIdUseCase: GetRecipesByIdUseCase,
    private val getCartItemUseCase: GetCartItemUseCase,
    private val favoriteUseCase: FavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    private val _getDataId = MutableStateFlow(AppState())
    val getDataId = _getDataId.asStateFlow()

    private val priceList = listOf(
        99,  149,199,249,299, 349,399, 449, 499,549, 555, 599, 650, 699,
        720, 750, 799, 850, 875, 899, 925, 950, 985, 999
    )

    init {
        getData()
        getCartItems()
        getFavorites()
        getCategories()
    }

    fun getFavorites() {
        viewModelScope.launch {
            favoriteUseCase.getFavorites().collect { favorites ->
                _state.update { it.copy(favoriteItems = favorites) }
            }
        }
    }

    fun isFavorite(recipeId: String): Flow<Boolean> {
        return favoriteUseCase.isFavorite(recipeId)
    }

    fun getCartItems() {
        viewModelScope.launch {
            getCartItemUseCase.getCartItems().collect { items ->
                val updatedItems = items.map { item ->
                    val mealId = item.recipe.idMeal.toIntOrNull() ?: 0
                    item.copy(recipe = item.recipe.copy(price = priceList[mealId % priceList.size]))
                }
                _state.update { it.copy(cartItems = updatedItems) }
            }
        }
    }

    fun getData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {

                val response = getRecipesUseCase.invoke()
                val recipesWithPrice = response.meals.map { item ->
                    val mealId = item.idMeal.toIntOrNull() ?: 0
                    item.copy(
                        price = priceList[mealId % priceList.size]
                    )
                }

                _state.update {
                    it.copy(
                        isLoading = false,
                        recipes = recipesWithPrice
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            try {
                val response = getRecipesUseCase.getCategories()
                _state.update {
                    it.copy(categories = response.categories)
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun searchFood(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val response = getRecipesUseCase.searchRecipes(query)
                val searchResults = response.meals.map { item ->
                    val mealId = item.idMeal.toIntOrNull() ?: 0
                    item.copy(price = priceList[mealId % priceList.size])
                } ?: emptyList()

                _state.update {
                    it.copy(isLoading = false, recipes = searchResults)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun getFoodByCategory(categoryName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val response = getRecipesUseCase.getRecipesByCategory(categoryName)
                val results = response.meals.map { item ->
                    val mealId = item.idMeal.toIntOrNull() ?: 0
                    item.copy(price = priceList[mealId % priceList.size])
                }

                _state.update {
                    it.copy(isLoading = false, recipes = results)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun getRecipesById(id: Int) {
        viewModelScope.launch {
            _getDataId.update { it.copy(isLoading = true, error = null) }

            try {
                val recipe = getRecipesByIdUseCase.invoke(id)



                val finalRecipe = recipe.copy(
                    price = priceList[id % priceList.size]
                )
                _getDataId.update { it.copy(isLoading = false, selectedRecipe = finalRecipe) }
            } catch (e: Exception) {
                _getDataId.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun toggleFavorite(recipe: Meal) {
        viewModelScope.launch {
            val isFav = favoriteUseCase.isFavorite(recipe.idMeal).first()
            if (isFav) {
                favoriteUseCase.deleteFavorite(recipe.idMeal)
            } else {
                favoriteUseCase.insertFavorite(recipe)
            }
        }
    }

    fun addToCart(recipe: Meal) {
        viewModelScope.launch {
            val existing = _state.value.cartItems.find { it.recipe.idMeal == recipe.idMeal }
            if (existing != null) {
                getCartItemUseCase.updateQuantity(recipe.idMeal, existing.quantity + 1)
            } else {
                getCartItemUseCase.addCart(recipe)
            }
        }
    }

    fun removeFromCart(recipeId: String) {
        viewModelScope.launch {
            val existing = _state.value.cartItems.find { it.recipe.idMeal == recipeId }
            if (existing != null) {
                if (existing.quantity > 1) {
                    getCartItemUseCase.updateQuantity(recipeId, existing.quantity - 1)
                } else {
                    getCartItemUseCase.remove(recipeId.toInt())
                }
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            getCartItemUseCase.clear()
        }
    }


}

data class AppState(
    val isLoading: Boolean = false,
    val recipes: List<Meal> = emptyList(),
    val selectedRecipe: MealX? = null,
    val error: String? = null,
    val categories: List<Category> = emptyList(),
    val cartItems: List<CartItem> = emptyList(),
    val favoriteItems: List<Meal> = emptyList(),
    val userLocation: LocationModel? = null
)

data class CartItem(
    val recipe: Meal,
    val quantity: Int
)
