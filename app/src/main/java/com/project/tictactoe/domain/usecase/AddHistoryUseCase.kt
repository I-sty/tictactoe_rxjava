package com.project.tictactoe.domain.usecase

import com.project.tictactoe.data.model.HistoryEntity
import com.project.tictactoe.data.repository.IHistoryRepository
import com.project.tictactoe.domain.model.Player
import com.project.tictactoe.domain.model.toDAOString
import org.koin.core.annotation.Factory
import java.util.Date

@Factory
class AddHistoryUseCase(private val historyRepository: IHistoryRepository) {
    suspend operator fun invoke(winner: Player, opponent: Player) {
        return historyRepository.addHistory(
            HistoryEntity(
                timestamp = Date().toDAOString(),
                playerName = winner.name,
                playerScore = winner.score,
                playerSymbol = winner.symbol,
                opponentSymbol = opponent.symbol,
                opponentName = opponent.name,
                opponentScore = opponent.score
            )
        )
    }
}