package com.example.cmpproject.model

import kotlinx.serialization.Serializable


@Serializable
data class LocationModel(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = ""
)
