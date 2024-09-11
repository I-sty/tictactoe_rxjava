package com.project.tictactoe.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.tictactoe.data.model.HistoryEntity

@Database(entities = [HistoryEntity::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}