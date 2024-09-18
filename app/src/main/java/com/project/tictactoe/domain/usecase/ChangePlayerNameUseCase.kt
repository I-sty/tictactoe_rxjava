package com.project.tictactoe.domain.usecase

import com.project.tictactoe.domain.model.GameState
import com.project.tictactoe.domain.model.GameStatus
import com.project.tictactoe.domain.model.Player
import io.reactivex.rxjava3.core.Observable
import org.koin.core.annotation.Factory

@Factory
class ChangePlayerNameUseCase {
    operator fun invoke(
        gameState: GameState,
        player1Name: String?,
        player2Name: String?
    ): Observable<GameState> {
        if (player1Name.isNullOrEmpty() || player2Name.isNullOrEmpty()) {
            return Observable.just(gameState)
        }
        val playerX = Player.X
        playerX.username = player1Name
        playerX.score = 0

        val playerO = Player.O
        playerO.username = player2Name
        playerO.score = 0

        return Observable.just(
            gameState.copy(
                error = null,
                loading = false,
                currentPlayer = playerX,
                playerX = playerX,
                playerO = playerO,
                board = Array(3) { Array(3) { Player.None } },
                winner = null,
                status = GameStatus.NOT_STARTED
            )
        )
    }
}