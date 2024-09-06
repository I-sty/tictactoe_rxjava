package com.project.tictactoe.domain.usecase

import com.project.tictactoe.data.repository.IHistoryRepository
import com.project.tictactoe.domain.model.History
import org.koin.core.annotation.Factory

@Factory
class DeleteHistoryUseCase(private val historyRepository: IHistoryRepository) {
    suspend fun deleteHistory(history: History) {
        return historyRepository.deleteHistory(history)
    }
}