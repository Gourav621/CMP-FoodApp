package com.example.cmpproject.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module



fun initKoin(
    platformModule: Module
) {
    startKoin {
        modules(
            shareModule,
            platformModule
        )
    }
}