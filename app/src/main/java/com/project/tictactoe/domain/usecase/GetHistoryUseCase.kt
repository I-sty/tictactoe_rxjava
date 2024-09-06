package com.project.tictactoe.domain.usecase

import com.project.tictactoe.core.mapFrom
import com.project.tictactoe.data.repository.IHistoryRepository
import com.project.tictactoe.domain.model.History
import com.project.tictactoe.domain.model.toDate
import org.koin.core.annotation.Factory

@Factory
class GetHistoryUseCase(private val historyRepository: IHistoryRepository) {
    suspend fun getHistory(): List<History> {
        return historyRepository.getHistory()
            .map {
                it.mapFrom {
                    History(
                        timestamp = it.timestamp.toDate(),
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
}
