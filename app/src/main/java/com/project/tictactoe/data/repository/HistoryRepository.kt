package com.project.tictactoe.data.repository

import com.project.tictactoe.data.local.AppDatabase
import com.project.tictactoe.data.model.HistoryEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import org.koin.core.annotation.Single

@Single
class HistoryRepositoryImpl(private val db: AppDatabase) : IHistoryRepository {
    override fun getHistory(): Maybe<List<HistoryEntity>> {
        return db.historyDao().getAll()
    }

    override fun addHistory(historyEntity: HistoryEntity): Completable {
        return db.historyDao().addHistory(historyEntity)
    }

    override fun deleteHistoryById(uid: Int): Maybe<Int> {
        return db.historyDao().deleteByUid(uid)
    }

    override fun deleteAllHistory(): Maybe<Int> {
        return db.historyDao().deleteAll()
    }
}