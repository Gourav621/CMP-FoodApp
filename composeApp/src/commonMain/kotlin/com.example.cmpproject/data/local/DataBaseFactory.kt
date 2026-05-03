package com.example.cmpproject.data.local

import app.cash.sqldelight.db.SqlDriver

expect class DataBaseFactory {
    fun createDriver(): SqlDriver
}
