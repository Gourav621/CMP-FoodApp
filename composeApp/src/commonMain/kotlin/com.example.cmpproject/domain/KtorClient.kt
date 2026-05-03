package com.example.cmpproject.domain

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {
    val client = HttpClient{
        install(ContentNegotiation) {
            json(json=Json{
              ignoreUnknownKeys =true
            })
        }
        install(HttpTimeout){
            requestTimeoutMillis = 10000
            connectTimeoutMillis =10000
            socketTimeoutMillis =10000
        }
        install(Logging){
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
}