package com.example.cmpproject

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.example.cmpproject.di.initKoin
import com.example.cmpproject.di.wasmJsModule
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(platformModule = wasmJsModule())
    ComposeViewport(document.body!!) {
        App()
    }
}
