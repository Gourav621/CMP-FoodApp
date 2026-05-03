package com.example.cmpproject.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cmpproject.RecipesViewModel
import com.example.cmpproject.presentation.navgation.Routes
import com.example.cmpproject.domain.payment.getPaymentHandler
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController, viewModel: RecipesViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val cartItems = state.cartItems
    val totalAmount = cartItems.sumOf { it.recipe.price * it.quantity }

    val deliveryFee = 40
val context = Locale.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            CheckoutBottomBar(totalAmount + deliveryFee,



            ){

                val paymentHandler = getPaymentHandler(activity = Unit)
                paymentHandler.startPayment(totalAmount + deliveryFee.toDouble()){success ->
                  if (success){
                      navController.navigate(Routes.OrderSuccess) {
                          popUpTo(Routes.Home) { inclusive = false }
                      }
                  }
                }
//

            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Address Section
            AddressSection(state.userLocation?.address ?: "Detecting location...") {
               // viewModel.fetchUserLocation()
            }

            // Order Summary
            OrderSummarySection(cartItems)

            // Bill Details
            BillDetailsSection(totalAmount, deliveryFee)
        }
    }
}

@Composable
fun AddressSection(address: String, onUpdateLocation: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFFC8019))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delivery Address", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onUpdateLocation) {
                    Text("CHANGE", color = Color(0xFFFC8019), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = address, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun OrderSummarySection(cartItems: List<com.example.cmpproject.CartItem>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            cartItems.forEach { item ->
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${item.recipe.strMeal} x ${item.quantity}", modifier = Modifier.weight(1f))
                    Text("₹${item.recipe.price * item.quantity}")
                }
            }
        }
    }
}

@Composable
fun BillDetailsSection(totalAmount: Int, deliveryFee: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Bill Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Item Total", color = Color.Gray)
                Text("₹$totalAmount")
            }
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), Arrangement.SpaceBetween) {
                Text("Delivery Fee", color = Color.Gray)
                Text("₹$deliveryFee")
            }
            HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("To Pay", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("₹${totalAmount + deliveryFee}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun CheckoutBottomBar(totalToPay: Int, onPay: () -> Unit) {
    Surface(shadowElevation = 8.dp, color = Color.White) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp).navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("₹$totalToPay", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Text("TOTAL AMOUNT", fontSize = 10.sp, color = Color.Gray)
            }
            Button(
                onClick = {
                    onPay()


                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF48bb78)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(48.dp).width(180.dp)
            ) {
                Text("PAY NOW", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
        }
    }
}
