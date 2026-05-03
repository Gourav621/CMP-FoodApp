package com.example.cmpproject.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.cmpproject.RecipesViewModel
import com.example.cmpproject.model.Meal
import com.example.cmpproject.presentation.navgation.Routes
import com.example.cmpproject.utils.BottomBar
import com.example.cmpproject.utils.DrawerItem
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(navController: NavController) {
    val viewModel: RecipesViewModel = koinViewModel()
    val state = viewModel.state.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
val scope = rememberCoroutineScope()
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(key1 = Unit) {
        viewModel.getData()
        viewModel.getCategories()
    }


    ModalNavigationDrawer(
        drawerState = drawerState,

        drawerContent = {
            DrawerContent {
                scope.launch {
                    drawerState.close()
                }
            }

        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Food Delivery",  fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp) }, navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }){
                        Icon(imageVector = Icons.Default.Menu,contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.Black)
                    }
                }, actions = {
                    IconButton(onClick = {
                        navController.navigate(Routes.Cart)
                    }){
                        Icon(imageVector = Icons.Outlined.ShoppingCart,contentDescription = null,
                            modifier = Modifier.size(32.dp), tint = Color.Black)
                    }
                }, colors = TopAppBarDefaults.topAppBarColors( Color(0xFFF7F7F7)))
            }, bottomBar = {
                BottomBar(navController)
            }
        ) {paddingValues ->


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F7F7))
                    .padding(horizontal = 16.dp)
                    .padding(paddingValues)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Delicious food.\nSuperfast delivery.",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 34.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Search Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color.White)
                            .padding(horizontal = 16.dp).clickable {
                                navController.navigate(Routes.Search)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Search",
                            color = Color.LightGray,
                            modifier = Modifier.weight(1F)

                        )
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF6F00)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = {}){
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    // Categories
                    val stateCategories by viewModel.state.collectAsState()
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(stateCategories.categories.size) { index ->
                            val category = stateCategories.categories[index]
                            CategoryItem(
                                name = category.strCategory,
                                image = category.strCategoryThumb,
                                onClick = {
                                    navController.navigate(Routes.Category(category.strCategory))
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }


                item {
                    Text(
                        text = "Popular now",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (state.value.isLoading) {
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth().height(200.dp)
                        ) {

                            CircularProgressIndicator(color = Color(0xFFFF6F00))
                        }
                    }
                }

                if (state.value.error?.isNotEmpty() == true) {
                    item {
                        Text(state.value.error.toString(), color = Color.Red)
                    }
                }

                // Popular Items Grid-like list
                items(state.value.recipes.chunked(2)) { rowRecipes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowRecipes.forEach { recipe ->
                            PopularItemCard(
                                recipe = recipe,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    navController.navigate(Routes.Details(id = recipe.idMeal.toInt()))
                                }
                            )
                        }
                        if (rowRecipes.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

}

@Composable
@PreviewLightDark
fun CategoryItem(
    name: String,
    image: String,
    onClick: () -> Unit = {}
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for category icon
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = Color(0xFFF5F5F5)
            ) {
                AsyncImage(  model = image, contentDescription = null,
                    modifier = Modifier.size(70.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun PopularItemCard(recipe: Meal, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .padding(top = 30.dp) // Space for overlapping image
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(55.dp))
            Text(
                text = recipe.strMeal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                maxLines = 2,
                minLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "₹${recipe.price}",  fontWeight = FontWeight.Bold,
                    fontSize = 18.sp)
                Text("⭐️ ", fontSize = 18.sp)
            }
        }

        // Overlapping Circular Image
        AsyncImage(
            model = recipe.strMealThumb,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-30).dp)
                .clip(CircleShape)
                .border(4.dp, Color.White, CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun DrawerContent(onItemClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxHeight()

            .width(280.dp)
            .background(Color(0xFFFF6A00))
            .systemBarsPadding()// Orange color
            .padding(20.dp)
    ) {

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier
                .size(32.dp)
                .clickable { onItemClick() }
        )

        Spacer(modifier = Modifier.height(30.dp))

        DrawerItem("Offers and promo", icons = Icons.Outlined.LocalOffer, onItemClick = {})
        DrawerItem("Recent Orders", icons = Icons.Outlined.History, onItemClick = {})
        DrawerItem("Location", icons = Icons.Outlined.LocationOn, onItemClick = {})
        DrawerItem("Settings", icons = Icons.Outlined.Settings, onItemClick = {})
        DrawerItem("Security", icons = Icons.Outlined.Security, onItemClick = {})
        DrawerItem("Privacy Policy", icons = Icons.Outlined.Description, onItemClick = {})
    }
}

