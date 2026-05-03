package com.example.cmpproject

import androidx.compose.ui.window.ComposeUIViewController
import com.example.cmpproject.di.initKoin
import com.example.cmpproject.di.iosModule

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin(platformModule = iosModule())
    }
) {


    App() }