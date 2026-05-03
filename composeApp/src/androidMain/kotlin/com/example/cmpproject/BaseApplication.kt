package com.example.cmpproject

import android.app.Application
import com.example.cmpproject.di.androidModule
import com.example.cmpproject.di.initKoin
import org.koin.android.ext.koin.androidContext

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
       initKoin(
           platformModule = androidModule(this)
       )
    }
}
