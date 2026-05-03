
 // Created by Nainwaya on 23/04/26.

 import UIKit

 import Razorpay
 class PaymentDelegate: NSObject, RazorpayPaymentCompletionProtocolWithData {

     var razorpay: RazorpayCheckout?

     init(key: String) {
         super.init()
         razorpay = RazorpayCheckout.initWithKey(key, andDelegate: self)
     }

     func startPayment(amount: Double) {

         let options: [String: Any] = [
             "name": "Eatzy",
             "description": "Order Payment",
             "currency": "INR",
             "amount": Int(amount * 100),
             "theme": [
                 "color": "#FC8019"
             ],
             "prefill": [
                 "email": "test@razorpay.com",
                 "contact": "9987654321"
             ]
         ]

         guard let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
               let rootVC = scene.windows.first?.rootViewController else {
             return
         }

         razorpay?.open(options, displayController: rootVC)
     }

     // MARK: - Callbacks

     func onPaymentSuccess(_ payment_id: String, andData response: [AnyHashable : Any]?) {
         print("SUCCESS: \(payment_id)")
     }

     func onPaymentError(_ code: Int32, description str: String, andData response: [AnyHashable : Any]?) {
         print("FAILED: \(str)")
     }
 }
