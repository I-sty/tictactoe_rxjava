package com.project.tictactoe

import android.app.Application
import com.project.tictactoe.core.di.DatabaseModule
import com.project.tictactoe.core.di.DispatcherModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(DatabaseModule().module, DispatcherModule().module, defaultModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}