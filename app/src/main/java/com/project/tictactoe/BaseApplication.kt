package com.project.tictactoe

import android.app.Application
import com.project.tictactoe.di.DatabaseModule
import com.project.tictactoe.di.SchedulersModule
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
            modules(DatabaseModule().module, SchedulersModule().module, defaultModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}