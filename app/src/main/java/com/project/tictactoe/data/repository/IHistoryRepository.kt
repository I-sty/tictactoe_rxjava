package com.project.tictactoe.data.repository

import com.project.tictactoe.data.model.HistoryEntity

interface IHistoryRepository {
    suspend fun getHistory(): List<HistoryEntity>
    suspend fun addHistory(historyEntity: HistoryEntity)
    suspend fun deleteHistory(historyEntity: HistoryEntity)
}