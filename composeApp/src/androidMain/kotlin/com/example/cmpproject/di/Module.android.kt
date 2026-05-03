package com.example.cmpproject.di

import android.content.Context
import com.example.cmpproject.data.local.DataBaseFactory
import com.example.cmpproject.example.Database
import org.koin.dsl.module

fun androidModule(
    context: Context
) = module {
    single { DataBaseFactory(context) }
}