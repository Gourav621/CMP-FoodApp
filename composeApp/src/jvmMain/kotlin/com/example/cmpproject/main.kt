package com.example.cmpproject

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.cmpproject.di.initKoin
import com.example.cmpproject.di.jvmModule

fun main() = application {
    initKoin(platformModule = jvmModule())
    Window(
        onCloseRequest = ::exitApplication,
        title = "CmpProject",

    ) {
        App()
    }
}