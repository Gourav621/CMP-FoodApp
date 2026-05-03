package com.example.cmpproject.domain.UseCase

import com.example.cmpproject.domain.Recipes

class SaveLocationUseCase(private val repo: Recipes) {
    suspend fun saveLocation(latitude: Double, longitude: Double) {
        repo.saveLocation(latitude, longitude)
    }

}