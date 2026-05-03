package com.example.cmpproject.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.cmpproject.example.Database

actual class DataBaseFactory {

    actual fun createDriver(): SqlDriver {

        return NativeSqliteDriver(
            Database.Schema,
            "cmp.db"
        )
    }
}