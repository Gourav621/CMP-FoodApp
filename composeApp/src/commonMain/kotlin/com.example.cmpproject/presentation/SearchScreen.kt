package com.example.cmpproject.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cmpproject.presentation.navgation.Routes
import coil3.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.cmpproject.RecipesViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: RecipesViewModel = koinViewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    val recentSearches = listOf("Biryani", "Pizza", "Burger", "Dosa")
    val popularCuisines = listOf("North Indian", "Chinese", "South Indian", "Italian", "Desserts")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { 
                            searchQuery = it
                            if (it.isNotEmpty()) {
                                viewModel.searchFood(it)
                            }
                        },
                        placeholder = { Text("Search  dishes ", fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        leadingIcon = {
                           IconButton(onClick = {

                           }){
                               Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFFF6F00))
                           }
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            if (searchQuery.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Recent Searches",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(recentSearches) { search ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                searchQuery = search
                                viewModel.searchFood(search)
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = search, fontSize = 16.sp, color = Color.DarkGray)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Popular Cuisines",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(popularCuisines) { cuisine ->
                            SearchCuisineItem(cuisine) {
                                searchQuery = cuisine
                                viewModel.searchFood(cuisine)
                            }
                        }
                    }
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Showing results for \"$searchQuery\"",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                if (state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFFFF6F00))
                        }
                    }
                } else if (state.recipes.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No recipes found", color = Color.Gray)
                        }
                    }
                } else {
                    items(state.recipes) { recipe ->
                        SearchResultItem(recipe) {
                            navController.navigate(Routes.Details(recipe.idMeal.toInt()))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(recipe: com.example.cmpproject.model.Meal, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = recipe.strMealThumb,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = recipe.strMeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₹${recipe.price}",
                    color = Color(0xFFFF6F00),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SearchCuisineItem(name: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                color = Color(0xFFFFF3E0)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = name.take(1),
                        color = Color(0xFFFF6F00),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SearchResultItemPlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEEEEEE))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Box(modifier = Modifier.width(120.dp).height(16.dp).background(Color(0xFFEEEEEE)))
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.width(80.dp).height(12.dp).background(Color(0xFFF5F5F5)))
            }
        }
    }
}
