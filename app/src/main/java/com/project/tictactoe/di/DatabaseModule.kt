package com.project.tictactoe.core.di

import android.app.Application
import androidx.room.Room
import com.project.tictactoe.data.local.AppDatabase
import com.project.tictactoe.data.local.HistoryDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule {
    @Single
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "app_database")
            .build()
    }

    @Single
    fun provideDao(database: AppDatabase): HistoryDao {
        return database.historyDao()
    }
}