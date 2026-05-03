package com.example.cmpproject.di

import com.example.cmpproject.data.local.DataBaseFactory
import com.example.cmpproject.example.Database
import org.koin.dsl.module

fun iosModule() = module {
    single { DataBaseFactory() }
}