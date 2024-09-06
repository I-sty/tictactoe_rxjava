package com.project.tictactoe.ui

import com.project.tictactoe.domain.model.Player

data class TicTacToeGameState(
    val error: String? = null,
    val loading: Boolean = false,
    val currentPlayer: Player = Player.X,
    val board: Array<Array<Player>> = Array(3) { Array(3) { Player.None } },
    val winner: Player? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TicTacToeGameState

        if (loading != other.loading) return false
        if (currentPlayer != other.currentPlayer) return false
        if (!board.contentDeepEquals(other.board)) return false
        if (winner != other.winner) return false

        return true
    }

    override fun hashCode(): Int {
        var result = loading.hashCode()
        result = 31 * result + currentPlayer.hashCode()
        result = 31 * result + board.contentDeepHashCode()
        result = 31 * result + (winner?.hashCode() ?: 0)
        return result
    }
}