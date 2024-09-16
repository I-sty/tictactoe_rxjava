package com.project.tictactoe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.tictactoe.data.model.HistoryEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): Maybe<List<HistoryEntity>>

    @Insert
    fun addHistory(historyEntity: HistoryEntity): Completable

    @Query("DELETE FROM history WHERE uid = :uid")
    fun deleteByUid(uid: Int): Maybe<Int>

    @Query("DELETE FROM history")
    fun deleteAll(): Maybe<Int>
}