package com.project.tictactoe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.tictactoe.data.model.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<HistoryEntity>

    @Insert
    fun addHistory(historyEntity: HistoryEntity)

    @Query("DELETE FROM history WHERE uid = :uid")
    fun deleteByUid(uid: Int)

    @Query("DELETE FROM history")
    fun deleteAll()
}