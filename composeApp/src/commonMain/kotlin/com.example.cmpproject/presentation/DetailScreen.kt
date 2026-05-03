package com.example.cmpproject.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.example.cmpproject.RecipesViewModel
import com.example.cmpproject.model.MealX
import com.example.cmpproject.presentation.navgation.Routes

import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(recipeId: Int, navController: NavController) {
    val viewModel: RecipesViewModel = koinViewModel()
    val state by viewModel.getDataId.collectAsState()
    LaunchedEffect(recipeId) {
        viewModel.getRecipesById(recipeId)

    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val appState by viewModel.state.collectAsState()

    val isFavorite by viewModel.isFavorite(recipeId.toString()).collectAsState(initial = false)

    val isInCart = appState.cartItems.any { it.recipe.idMeal == state.selectedRecipe?.idMeal }
    Scaffold(
        bottomBar = {
            state.selectedRecipe?.let { recipe ->
                DetailBottomBar(
                    isInCart = isInCart,
                    onAddToCart = {

                        //viewModel.addToCart(recipe)
                        if (!isInCart){
                            viewModel.addToCart(
                                com.example.cmpproject.model.Meal(
                                    idMeal = recipe.idMeal ?: "",
                                    strMeal = recipe.strMeal ?: "",
                                    strMealThumb = recipe.strMealThumb ?: "",
                                    price = recipe.price,
                                    favorite = recipe.favorite
                                )
                            )


                        scope.launch {
                            snackbarHostState.showSnackbar("Added to cart ✅")
                        }
                        } else {
                            navController.navigate(Routes.Cart)
                        }
                    },
                    onBuyNowClick = {

                        navController.navigate(Routes.Checkout(1))
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFFC8019)
                    )
                }
                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                state.selectedRecipe != null -> {
                    DetailContent(
                        recipe = state.selectedRecipe!!,
                        isFavorite = isFavorite,
                        onBack = { navController.popBackStack() },
                        onFavoriteToggle = {
                            state.selectedRecipe?.let { recipe ->
                                viewModel.toggleFavorite(
                                    com.example.cmpproject.model.Meal(
                                        idMeal = recipe.idMeal ?: "",
                                        strMeal = recipe.strMeal ?: "",
                                        strMealThumb = recipe.strMealThumb ?: "",
                                        price = recipe.price,
                                        favorite = !isFavorite
                                    )
                                )
                            }
                        },
                        onAddToCart = {
                            state.selectedRecipe?.let { recipe ->
                                viewModel.addToCart(
                                    com.example.cmpproject.model.Meal(
                                        idMeal = recipe.idMeal ?: "",
                                        strMeal = recipe.strMeal ?: "",
                                        strMealThumb = recipe.strMealThumb ?: "",
                                        price = recipe.price,
                                        favorite = recipe.favorite
                                    )
                                )
                                scope.launch {
                                    snackbarHostState.showSnackbar("Added to cart ✅")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DetailContent(
    recipe: MealX,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onAddToCart: () -> Unit
) {
    val swiggyOrange = Color(0xFFFC8019)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            SubcomposeAsyncImage(
                model = recipe.strMealThumb,
                contentDescription = recipe.strMeal,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(16.dp).size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.8f)).align(Alignment.TopStart)
            ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Black) }

            IconButton(
                onClick = onFavoriteToggle,
                modifier = Modifier.padding(16.dp).size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.8f)).align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.Black
                )
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = recipe.strMeal ?: "", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                    Text(text = recipe.strCategory ?: "", color = Color.Gray, style = MaterialTheme.typography.bodyLarge)
                }

            }

            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Color(0xFF60B246), modifier = Modifier.size(20.dp))
                Text(" 4.2 (100+ ratings)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(" • 25-30 mins", color = Color.Gray)
            }

            Text(
                text = "₹${recipe.price}", 
                style = MaterialTheme.typography.headlineSmall, 
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), thickness = 0.5.dp)

            // Ingredients
            Text(text = "Ingredients", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(12.dp))
            
            val ingredients = listOfNotNull(
                recipe.strIngredient1, recipe.strIngredient2, recipe.strIngredient3,
                recipe.strIngredient4, recipe.strIngredient5, recipe.strIngredient6
            ).filter { it.isNotEmpty() }
            
            ingredients.forEach { ingredient ->
                Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.Gray))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = ingredient, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Instructions
            Text(text = "Preparation", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = recipe.strInstructions ?: "No instructions available",
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun DetailBottomBar(isInCart: Boolean, onAddToCart: () -> Unit, onBuyNowClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 12.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onAddToCart,
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFC8019))
            ) {
                Text(
                    if (isInCart) "GO TO CART" else "ADD TO CART",
                    color = Color(0xFFFC8019),
                    fontWeight = FontWeight.ExtraBold
                )

            }
            Button(
                onClick = onBuyNowClick,
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFC8019))
            ) {
                Text("BUY NOW", color = Color.White, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}
