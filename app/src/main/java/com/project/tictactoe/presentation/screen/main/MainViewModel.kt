package com.project.tictactoe.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tictactoe.domain.model.GameState
import com.project.tictactoe.domain.model.GameStatus
import com.project.tictactoe.domain.model.Player
import com.project.tictactoe.domain.usecase.AddHistoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val addHistoryUseCase: AddHistoryUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    fun handleEvent(event: GameEvent) {
        when (event) {
            is GameEvent.CellClicked -> onCellClick(event.row, event.col)
            is GameEvent.PlayerChanged -> onPlayerChange(event.player)
            GameEvent.RestartClicked -> restartGame()
            GameEvent.ResumeClicked -> resumeGame()
            GameEvent.ShowWinnerDialog -> {
                _state.value = _state.value.copy(showWinnerPopup = true)
            }

            GameEvent.OnDismissWinnerDialogClicked -> {
                _state.value = _state.value.copy(showWinnerPopup = false)
                newMatch()
            }
        }
    }

    /**
     * Start a new game with the current player as the winner.
     */
    internal fun newMatch() {
        val winner = _state.value.winner ?: Player.X
        _state.value =
            _state.value.copy(
                error = null,
                loading = false,
                currentPlayer = winner,
                board = Array(3) { Array(3) { Player.None } },
                winner = null,
                status = GameStatus.NOT_STARTED
            )
    }

    private fun resumeGame() {
        //todo resume game
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
            _state.value.copy(
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
        _state.value = _state.value.copy(currentPlayer = player)
    }

    private fun getNextPlayer(): Player {
        return if (_state.value.currentPlayer.symbol == Player.X.symbol) {
            _state.value.playerO
        } else {
            _state.value.playerX
        }
    }

    private fun onCellClick(row: Int, col: Int) {
        if (_state.value.board[row][col] == Player.None && _state.value.winner == null) {
            val updatedBoard = _state.value.board.copyOf()
            updatedBoard[row][col] = _state.value.currentPlayer
            val winner: Player? = checkWinner(updatedBoard)
            val nextPlayer = getNextPlayer()
            if (winner != null) {
                winner.score++
                viewModelScope.launch(ioDispatcher) {
                    addHistoryUseCase(_state.value.playerX, _state.value.playerO)
                }
                _state.value =
                    _state.value.copy(
                        board = updatedBoard,
                        winner = winner,
                        status = GameStatus.ENDED_WITH_WINNER,
                        currentPlayer = nextPlayer
                    )
            } else {
                _state.value =
                    _state.value.copy(
                        board = updatedBoard,
                        winner = winner,
                        currentPlayer = nextPlayer
                    )
            }
            if (boardIsFull(updatedBoard)) {
                _state.value = _state.value.copy(
                    winner = Player.None,
                    status = GameStatus.ENDED_WITHOUT_WINNER
                )
                viewModelScope.launch(ioDispatcher) {
                    addHistoryUseCase(_state.value.playerX, _state.value.playerO)
                }
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