package com.project.tictactoe.ui

import androidx.lifecycle.ViewModel
import com.project.tictactoe.data.repository.IGameRepository
import com.project.tictactoe.domain.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(private val gameRepository: IGameRepository) : ViewModel() {
    private val _state = MutableStateFlow(TicTacToeGameState())
    val state: StateFlow<TicTacToeGameState> = _state

    fun handleEvent(event: GameEvent) {
        when (event) {
            is GameEvent.CellClicked -> onCellClick(event.row, event.col)
            GameEvent.GameLoaded -> {}
            GameEvent.MenuClicked -> showMenu()
            is GameEvent.PlayerChanged -> onPlayerChange(event.player)
            GameEvent.StartClicked -> {}
        }
    }

    private fun showMenu() {
        // TODO("Not yet implemented")
    }

    private fun onPlayerChange(player: Player) {
        _state.value = _state.value.copy(currentPlayer = player)
    }

    private fun onCellClick(row: Int, col: Int) {
        if (_state.value.board[row][col] == Player.None && _state.value.winner == null) {
            val updatedBoard = _state.value.board.copyOf()
            updatedBoard[row][col] = _state.value.currentPlayer
            val winner: Player? = checkWinner(updatedBoard)
            val nextPlayer = if (_state.value.currentPlayer == Player.X) Player.O else Player.X
            _state.value =
                _state.value.copy(board = updatedBoard, winner = winner, currentPlayer = nextPlayer)
        }
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