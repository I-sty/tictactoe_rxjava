package com.project.tictactoe.presentation.screen.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.tictactoe.domain.model.GameState
import com.project.tictactoe.domain.model.GameStatus
import com.project.tictactoe.domain.model.Player
import com.project.tictactoe.domain.usecase.AddHistoryUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val addHistoryUseCase: AddHistoryUseCase,
) : ViewModel() {
    private val _state: MutableLiveData<GameState> = MutableLiveData(GameState())
    val state: LiveData<GameState> = _state

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun handleEvent(event: GameEvent) {
        when (event) {
            is GameEvent.CellClicked -> onCellClick(event.row, event.col)
            is GameEvent.PlayerChanged -> onPlayerChange(event.player)
            GameEvent.RestartClicked -> restartGame()
            GameEvent.ShowWinnerDialog -> {
                _state.value = _state.value?.copy(showWinnerPopup = true)
            }

            GameEvent.OnDismissWinnerDialogClicked -> {
                _state.value = _state.value?.copy(showWinnerPopup = false)
                newMatch()
            }

            is GameEvent.PlayerNameChanged -> {
                if (event.player1Name != null && event.player2Name != null) {
                    val playerX = Player.X
                    playerX.username = event.player1Name
                    playerX.score = 0

                    val playerO = Player.O
                    playerO.username = event.player2Name
                    playerO.score = 0

                    _state.value =
                        _state.value?.copy(
                            error = null,
                            loading = false,
                            currentPlayer = playerX,
                            playerX = playerX,
                            playerO = playerO,
                            board = Array(3) { Array(3) { Player.None } },
                            winner = null,
                            status = GameStatus.NOT_STARTED
                        )
                }
            }

            GameEvent.DrawGameDialog -> _state.value = _state.value?.copy(showDrawGamePopup = true)
            GameEvent.OnDismissDrawGameDialogClicked -> {
                _state.value =
                    _state.value?.copy(showDrawGamePopup = false)
                newMatch()
            }
        }
    }

    /**
     * Start a new game with the current player as the winner.
     */
    private fun newMatch() {
        val winner = _state.value?.winner ?: Player.X
        _state.value =
            _state.value?.copy(
                error = null,
                loading = false,
                currentPlayer = winner,
                board = Array(3) { Array(3) { Player.None } },
                winner = null,
                status = GameStatus.NOT_STARTED
            )
    }

    /**
     * Restart the game to its initial state.
     */
    internal fun restartGame() {
        val playerX = Player.X
        playerX.score = 0

        val playerO = Player.O
        playerO.score = 0

        _state.value =
            _state.value?.copy(
                error = null,
                loading = false,
                currentPlayer = playerX,
                playerX = playerX,
                playerO = playerO,
                board = Array(3) { Array(3) { Player.None } },
                winner = null,
                status = GameStatus.NOT_STARTED
            )
    }

    private fun onPlayerChange(player: Player) {
        _state.value = _state.value?.copy(currentPlayer = player)
    }

    private fun getNextPlayer(): Player? {
        return if (_state.value?.currentPlayer?.symbol == Player.X.symbol) {
            _state.value?.playerO
        } else {
            _state.value?.playerX
        }
    }

    private fun onCellClick(row: Int, col: Int) {
        if (_state.value?.board?.get(row)
                ?.get(col) == Player.None && _state.value!!.winner == null
        ) {
            val updatedBoard = _state.value!!.board.copyOf()
            updatedBoard[row][col] = _state.value!!.currentPlayer
            val winner: Player? = checkWinner(updatedBoard)
            val nextPlayer = getNextPlayer()
            if (winner != null) {
                winner.score++
                val disposable =
                    addHistoryUseCase(
                        winner,
                        nextPlayer!!
                    ).observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.i("History", "History added successfully")
                        }, { throwable ->
                            Log.e("History", "Error adding history", throwable)
                        })
                disposables.add(disposable)
                _state.value =
                    _state.value!!.copy(
                        board = updatedBoard,
                        winner = winner,
                        status = GameStatus.ENDED_WITH_WINNER,
                        currentPlayer = nextPlayer
                    )
            } else {
                _state.value =
                    _state.value!!.copy(
                        board = updatedBoard,
                        winner = null,
                        currentPlayer = nextPlayer!!
                    )
            }
            if (boardIsFull(updatedBoard)) {
                _state.value = _state.value!!.copy(
                    winner = Player.None,
                    status = GameStatus.ENDED_WITHOUT_WINNER
                )
                val disposable =
                    addHistoryUseCase(_state.value!!.playerX, _state.value!!.playerO).observeOn(
                        AndroidSchedulers.mainThread()
                    ).subscribe({
                        Log.i("History", "History added successfully")
                    }, { throwable ->
                        Log.e("History", "Error adding history", throwable)
                    })
                disposables.add(disposable)
            }
        }
    }

    private fun boardIsFull(updatedBoard: Array<Array<Player>>): Boolean {
        return updatedBoard.any { row -> row.any { it == Player.None } }.not()
    }

    private fun checkWinner(board: Array<Array<Player>>): Player? {
        // Check rows
        for (row in 0..2) {
            if (board[row][0] != Player.None && board[row][0] == board[row][1] && board[row][0] == board[row][2]) {
                return board[row][0]
            }
        }

        // Check columns
        for (col in 0..2) {
            if (board[0][col] != Player.None && board[0][col] == board[1][col] && board[0][col] == board[2][col]) {
                return board[0][col]
            }
        }

        // Check diagonals
        if (board[0][0] != Player.None && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            return board[0][0]
        }
        if (board[0][2] != Player.None && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            return board[0][2]
        }

        return null // No winner yet
    }
}