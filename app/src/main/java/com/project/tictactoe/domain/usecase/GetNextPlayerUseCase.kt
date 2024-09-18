package com.project.tictactoe.domain.usecase

import com.project.tictactoe.domain.model.GameState
import com.project.tictactoe.domain.model.Player
import org.koin.core.annotation.Factory

@Factory
class GetNextPlayerUseCase {

    operator fun invoke(currentState: GameState): Player {
        return if (currentState.currentPlayer.symbol == Player.X.symbol) {
            currentState.playerO
        } else {
            currentState.playerX
        }
    }
}