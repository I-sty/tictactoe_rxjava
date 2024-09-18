package com.project.tictactoe.domain.usecase

import com.project.tictactoe.domain.model.GameState
import com.project.tictactoe.domain.model.GameStatus
import com.project.tictactoe.domain.model.Player
import io.reactivex.rxjava3.core.Observable
import org.koin.core.annotation.Factory

@Factory
/**
 * Restart the game to its initial state.
 */
class RestartGameUseCase {

    operator fun invoke(currentState: GameState): Observable<GameState> {
        val playerX = Player.X
        playerX.score = 0

        val playerO = Player.O
        playerO.score = 0

        return Observable.just(
            currentState.copy(
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
