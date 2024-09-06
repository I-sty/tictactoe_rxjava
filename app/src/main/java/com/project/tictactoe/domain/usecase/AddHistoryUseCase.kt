package com.project.tictactoe.domain.usecase

import com.project.tictactoe.data.repository.IHistoryRepository
import com.project.tictactoe.domain.model.History
import org.koin.core.annotation.Factory

@Factory
class AddHistoryUseCase(private val historyRepository: IHistoryRepository) {
    suspend fun addHistory(history: History) {
        return historyRepository.addHistory(history)
    }
}