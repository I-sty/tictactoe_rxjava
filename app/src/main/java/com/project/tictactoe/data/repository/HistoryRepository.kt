package com.project.tictactoe.data.repository

import com.project.tictactoe.data.local.AppDatabase
import com.project.tictactoe.data.model.HistoryEntity
import org.koin.core.annotation.Single

@Single
class HistoryRepositoryImpl(private val db: AppDatabase) : IHistoryRepository {
    override suspend fun getHistory(): List<HistoryEntity> {
        return db.historyDao().getAll()
    }

    override suspend fun addHistory(historyEntity: HistoryEntity) {
        return db.historyDao().addHistory(historyEntity)
    }

    override suspend fun deleteHistoryById(uid: Int) {
        return db.historyDao().deleteByUid(uid)
    }

    override suspend fun deleteAllHistory() {
        return db.historyDao().deleteAll()
    }
}