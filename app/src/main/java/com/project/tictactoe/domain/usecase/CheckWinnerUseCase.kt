package com.project.tictactoe.domain.usecase

import com.project.tictactoe.domain.model.Player
import org.koin.core.annotation.Factory

@Factory
class CheckWinnerUseCase {

    operator fun invoke(board: Array<Array<Player>>): Player? {
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