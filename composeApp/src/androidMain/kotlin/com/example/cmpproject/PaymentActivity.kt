package com.example.cmpproject

import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Toast
import com.example.cmpproject.domain.payment.PaymentHandler
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class PaymentActivity(
    private val activity: MainActivity?
) : PaymentHandler,
    PaymentResultWithDataListener,
    ExternalWalletListener,
    DialogInterface.OnClickListener {

    private val alertDialog = AlertDialog.Builder(activity)

    init {
        Checkout.preload(activity?.applicationContext)

        alertDialog.setTitle("Payment Status")
        alertDialog.setCancelable(true)
        alertDialog.setPositiveButton("OK", this)
    }

    override fun startPayment(amount: Double, onResult: (Boolean) -> Unit) {

        val co = Checkout()
        co.setKeyID("rzp_test_RV0T2VwVaZJ52J")

        try {
            val options = JSONObject()

            options.put("name", "Eatzy")
            options.put("description", "Order Payment")
            options.put("currency", "INR")
            options.put("amount", (amount * 100).toInt())
            val themeObject = JSONObject()
            themeObject.put("color", "#FC8019") // Swig exact orange
            options.put("theme", themeObject)


            val prefill = JSONObject()
            prefill.put("email", "test@razorpay.com")
            prefill.put("contact", "9987654321")

            options.put("prefill", prefill)

            co.open(activity, options)

        } catch (e: Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        alertDialog.setMessage("Success: $p0")
        alertDialog.show()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        alertDialog.setMessage("Failed: $p1")
        alertDialog.show()
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        alertDialog.setMessage("External Wallet Selected")
        alertDialog.show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dialog?.dismiss()
    }
}