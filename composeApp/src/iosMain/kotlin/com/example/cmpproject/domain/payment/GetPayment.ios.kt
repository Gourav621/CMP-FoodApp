package com.example.cmpproject.domain.payment



actual fun getPaymentHandler(activity: Any): PaymentHandler {
    return PaymentIos()
}

