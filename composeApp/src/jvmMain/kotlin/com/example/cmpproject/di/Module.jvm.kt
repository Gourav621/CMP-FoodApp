package com.example.cmpproject.di

import com.example.cmpproject.data.local.DataBaseFactory
import org.koin.dsl.module

fun jvmModule() = module {
    single { DataBaseFactory() }
}
