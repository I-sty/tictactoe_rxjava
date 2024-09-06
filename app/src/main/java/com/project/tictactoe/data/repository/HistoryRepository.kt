package com.project.tictactoe.data.repository

import com.project.tictactoe.data.local.HistoryDao
import com.project.tictactoe.data.model.HistoryEntity
import org.koin.core.annotation.Single

@Single
class HistoryRepositoryImpl(private val dao: HistoryDao) : IHistoryRepository {
    override suspend fun getHistory(): List<HistoryEntity> {
        return dao.getAll()
    }

    override suspend fun addHistory(historyEntity: HistoryEntity) {
        return dao.addHistory(historyEntity)
    }

    override suspend fun deleteHistory(historyEntity: HistoryEntity) {
        return dao.delete(historyEntity)
    }
}