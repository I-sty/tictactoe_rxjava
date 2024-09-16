package com.project.tictactoe.data.repository

import com.project.tictactoe.data.model.HistoryEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface IHistoryRepository {
    fun getHistory(): Maybe<List<HistoryEntity>>
    fun addHistory(historyEntity: HistoryEntity): Completable
    fun deleteHistoryById(uid: Int): Maybe<Int>
    fun deleteAllHistory(): Maybe<Int>
}