package com.example.cmpproject.di

import com.example.cmpproject.RecipesViewModel
import com.example.cmpproject.data.RepoImpl
import com.example.cmpproject.data.local.DataBaseFactory

import com.example.cmpproject.data.local.SqlDelightDataSource
import com.example.cmpproject.domain.KtorClient
import com.example.cmpproject.domain.Recipes
import com.example.cmpproject.domain.UseCase.FavoriteUseCase
import com.example.cmpproject.domain.UseCase.GetCartItemUseCase
import com.example.cmpproject.domain.UseCase.GetCurrentLocationUseCase
import com.example.cmpproject.domain.UseCase.GetRecipesByIdUseCase
import com.example.cmpproject.domain.UseCase.GetRecipesUseCase
import com.example.cmpproject.example.Database

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val shareModule = module {

    // ✅ Ktor Client
    single { KtorClient }

    // ✅ SQLDelight Database setup
    single {
        val driver = get<DataBaseFactory>().createDriver()
        Database(driver)
    }

    // ✅ Data Source
    singleOf(::SqlDelightDataSource)

    // ✅ Repository
    single<Recipes> { RepoImpl(get(), get()) }

    // ✅ UseCases
    singleOf(::GetRecipesUseCase)
    singleOf(::GetRecipesByIdUseCase)
    singleOf(::GetCurrentLocationUseCase)
    singleOf(::GetCartItemUseCase)
    singleOf(::FavoriteUseCase)

    // ✅ ViewModel
    viewModelOf(::RecipesViewModel)
}

//val shareModule = module {
//    single { KtorClient }
//
//    // Database
//    singleOf(::SqlDelightDataSource)
//
//
//    // Repository
//    single<Recipes> { RepoImpl(get(), get()) }
//
//    // UseCases
//    singleOf(::GetRecipesUseCase)
//    singleOf(::GetRecipesByIdUseCase)
//    singleOf(::GetCurrentLocationUseCase)
//    singleOf(::GetCartItemUseCase)
//
//    // ViewModel
//    viewModelOf(::RecipesViewModel)
//}


