package com.example.cmpproject

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform