package com.example.cmpproject.domain.payment

import com.example.cmpproject.MainActivity
import com.example.cmpproject.PaymentActivity

actual fun getPaymentHandler(activity: Any): PaymentHandler {
    val androidActivity =  MainActivity.activityInstance
    return PaymentActivity(androidActivity)


}
//rzp_test_RV0T2VwVaZJ52J