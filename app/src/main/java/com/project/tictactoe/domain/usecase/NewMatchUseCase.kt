package com.project.tictactoe.domain.usecase

import com.project.tictactoe.domain.model.GameState
import com.project.tictactoe.domain.model.GameStatus
import com.project.tictactoe.domain.model.Player
import io.reactivex.rxjava3.core.Observable
import org.koin.core.annotation.Factory

@Factory
/**
 * Start a new game with the current player as the winner.
 */
class NewMatchUseCase {

    operator fun invoke(gameState: GameState): Observable<GameState> {
        val winner = gameState.winner ?: Player.X
        return Observable.just(
            gameState.copy(
                error = null,
                loading = false,
                currentPlayer = winner,
                board = Array(3) { Array(3) { Player.None } },
                winner = null,
                status = GameStatus.NOT_STARTED
            )
        )
    }
}