package com.example.cmpproject.domain.payment

interface PaymentHandler {
    fun startPayment(amount: Double,onResult: (Boolean) -> Unit)
}