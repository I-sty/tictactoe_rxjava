package com.project.tictactoe.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.project.tictactoe.data.model.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM historyentity")
    fun getAll(): List<HistoryEntity>

    @Insert
    fun addHistory(historyEntity: HistoryEntity)

    @Delete
    fun delete(historyEntity: HistoryEntity)
}