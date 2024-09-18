package com.project.tictactoe.domain.usecase

import com.project.tictactoe.domain.model.GameState
import com.project.tictactoe.domain.model.GameStatus
import com.project.tictactoe.domain.model.Player
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.annotation.Factory

@Factory
class HandleCellClickUseCase(
    private val addHistoryUseCase: AddHistoryUseCase,
    private val getNextPlayerUseCase: GetNextPlayerUseCase,
    private val boardIsFullUseCase: BoardIsFullUseCase,
    private val checkWinnerUseCase: CheckWinnerUseCase,
    private val ioScheduler: Scheduler
) {

    operator fun invoke(
        currentState: GameState,
        row: Int,
        col: Int
    ): Observable<GameState> {
        return Observable.defer {
            if (currentState.board[row][col] == Player.None && currentState.winner == null) {
                val updatedBoard = currentState.board.copyOf()
                updatedBoard[row][col] = currentState.currentPlayer
                var winner = checkWinnerUseCase(updatedBoard)
                val nextPlayer = getNextPlayerUseCase(currentState)

                if (winner != null) {
                    winner.score++
                    return@defer addHistoryUseCase(winner, nextPlayer)
                        .subscribeOn(ioScheduler)
                        .flatMap {
                            Observable.just(
                                currentState.copy(
                                    board = updatedBoard,
                                    winner = winner,
                                    status = GameStatus.ENDED_WITH_WINNER,
                                    currentPlayer = nextPlayer
                                )
                            )
                        }
                } else {
                    currentState.run {
                        board = updatedBoard
                        winner = null
                        status = GameStatus.ENDED_WITHOUT_WINNER
                        currentPlayer = nextPlayer
                    }
                }

                if (boardIsFullUseCase(updatedBoard)) {
                    return@defer addHistoryUseCase(currentState.playerX, currentState.playerO)
                        .subscribeOn(Schedulers.io())
                        .flatMap {
                            Observable.just(
                                currentState.copy(
                                    board = updatedBoard,
                                    winner = Player.None,
                                    status = GameStatus.ENDED_WITHOUT_WINNER,
                                    currentPlayer = nextPlayer,
                                )
                            )
                        }
                }
            }
            return@defer Observable.just(currentState)
        }
    }
}