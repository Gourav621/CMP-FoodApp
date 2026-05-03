package com.example.cmpproject.domain.UseCase

import com.example.cmpproject.domain.Recipes
import com.example.cmpproject.model.LocationModel

class GetCurrentLocationUseCase(private val repo: Recipes) {
    suspend fun getCurrentLocation(): LocationModel {
        return repo.getCurrentLocation()
    }

}