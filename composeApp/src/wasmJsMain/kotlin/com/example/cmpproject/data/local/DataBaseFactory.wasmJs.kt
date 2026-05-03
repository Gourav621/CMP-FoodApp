package com.example.cmpproject.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.example.cmpproject.example.Database
import org.w3c.dom.Worker

actual class DataBaseFactory {
    @OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
    actual fun createDriver(): SqlDriver {
        val worker = Worker(
            js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
        )
        return WebWorkerDriver(worker).also {
            Database.Schema.create(it)
        }
    }
}

