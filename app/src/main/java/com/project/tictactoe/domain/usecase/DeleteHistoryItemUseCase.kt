package com.project.tictactoe.domain.usecase

import com.project.tictactoe.data.repository.IHistoryRepository
import org.koin.core.annotation.Factory

@Factory
class DeleteHistoryItemUseCase(private val historyRepository: IHistoryRepository) {
    suspend operator fun invoke(uid: Int) {
        historyRepository.deleteHistoryById(uid)
    }
}