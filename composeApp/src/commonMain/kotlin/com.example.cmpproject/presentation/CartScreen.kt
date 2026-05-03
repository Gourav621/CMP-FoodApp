package com.example.cmpproject.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.cmpproject.CartItem
import com.example.cmpproject.RecipesViewModel
import com.example.cmpproject.domain.payment.getPaymentHandler
import com.example.cmpproject.presentation.navgation.Routes
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: RecipesViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val cartItems = state.cartItems
    val totalAmount = cartItems.sumOf { it.recipe.price *it.quantity } // Using calories as price for demo
    val deliveryFee = 40
    LaunchedEffect(key1 = Unit){
        viewModel.getCartItems()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                CartBottomBar(totalAmount) {
                    val paymentHandler = getPaymentHandler(activity = Unit)
                    paymentHandler.startPayment(totalAmount + deliveryFee.toDouble()){success ->
                        if (success){
                            navController.navigate(Routes.OrderSuccess) {
                                popUpTo(Routes.Home) { inclusive = false }
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Your cart is empty", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F7F7))
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cartItems) { item ->
                    CartItemRow(
                        item = item,
                        onAdd = { viewModel.addToCart(item.recipe) },
                        onRemove = { viewModel.removeFromCart(item.recipe.idMeal) }
                    )
                }

                item {
                    BillDetails(totalAmount)
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onAdd: () -> Unit, onRemove: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.recipe.strMealThumb,
                contentDescription = item.recipe.strMeal,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.recipe.strMeal, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "₹${item.recipe.price} x ${item.quantity}", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "₹${item.recipe.price * item.quantity}", fontWeight = FontWeight.ExtraBold, color = Color.Black)
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFF3E0))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = Color(0xFFFF6F00))
                }
                Text(text = "${item.quantity}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = onAdd, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFFF6F00))
                }
            }
        }
    }
}

@Composable
fun BillDetails(totalAmount: Int) {
    val deliveryFee = 40
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Bill Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Item Total", color = Color.Gray)
                Text("₹$totalAmount")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Delivery Fee", color = Color.Gray)
                Text("₹$deliveryFee", color = Color(0xFF4CAF50))
            }
            HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Total Pay", fontWeight = FontWeight.Bold)
                Text("₹${totalAmount + deliveryFee}", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CartBottomBar(totalAmount: Int, onProceed: () -> Unit) {
    val deliveryFee = 40
    Surface(
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "₹${totalAmount + deliveryFee}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Text(text = "VIEW DETAILED BILL", fontSize = 10.sp, color = Color(0xFFFF6F00), fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onProceed,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(48.dp).width(160.dp)
            ) {
                Text("Proceed to Pay", fontWeight = FontWeight.Bold)
            }
        }
    }
}
