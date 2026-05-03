package com.example.cmpproject.domain.payment

class PaymentIos : PaymentHandler {
    override fun startPayment(amount: Double, onResult: (Boolean) -> Unit) {
        println("KMP: Payment trigger for amount $amount")
        iosPaymentBridge?.invoke(amount)
    }
}

var iosPaymentBridge: ((Double) -> Unit)? = null
