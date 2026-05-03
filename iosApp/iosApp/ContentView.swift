import UIKit
import SwiftUI
import ComposeApp
import Foundation
import Razorpay

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let paymentDelegate = PaymentDelegate(key: "rzp_test_RV0T2VwVaZJ52J")

        // Match the bridge name in PaymentIos.kt
        PaymentIosKt.iosPaymentBridge = { amount in
            paymentDelegate.startPayment(amount: amount.doubleValue)
        }

        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}

