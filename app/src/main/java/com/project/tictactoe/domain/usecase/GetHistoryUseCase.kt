package com.project.tictactoe.domain.usecase

import com.project.tictactoe.data.repository.IHistoryRepository
import com.project.tictactoe.domain.model.History
import com.project.tictactoe.domain.model.toDAODate
import org.koin.core.annotation.Factory

@Factory
class GetHistoryUseCase(private val historyRepository: IHistoryRepository) {
    suspend operator fun invoke(): List<History> {
        return historyRepository.getHistory()
            .map {
                History(
                    uid = it.uid,
                    timestamp = it.timestamp.toDAODate(),
                    playerName = it.playerName,
                    playerScore = it.playerScore,
                    playerSymbol = it.playerSymbol,
                    opponentSymbol = it.opponentSymbol,
                    opponentName = it.opponentName,
                    opponentScore = it.opponentScore
                )
            }
    }
}