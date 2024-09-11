package com.project.tictactoe.domain.model

data class GameState(
    val error: String? = null,
    val loading: Boolean = false,
    val currentPlayer: Player = Player.X,
    val playerX: Player = Player.X,
    val playerO: Player = Player.O,
    val board: Array<Array<Player>> = Array(3) { Array(3) { Player.None } },
    val winner: Player? = null,
    var status: GameStatus = GameStatus.NOT_STARTED,
    var showWinnerPopup: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (error != other.error) return false
        if (loading != other.loading) return false
        if (currentPlayer != other.currentPlayer) return false
        if (playerX != other.playerX) return false
        if (playerO != other.playerO) return false
        if (!board.contentDeepEquals(other.board)) return false
        if (winner != other.winner) return false
        if (status != other.status) return false
        if (showWinnerPopup != other.showWinnerPopup) return false

        return true
    }

    override fun hashCode(): Int {
        var result = error?.hashCode() ?: 0
        result = 31 * result + loading.hashCode()
        result = 31 * result + currentPlayer.hashCode()
        result = 31 * result + playerX.hashCode()
        result = 31 * result + playerO.hashCode()
        result = 31 * result + board.contentDeepHashCode()
        result = 31 * result + (winner?.hashCode() ?: 0)
        result = 31 * result + status.hashCode()
        result = 31 * result + showWinnerPopup.hashCode()
        return result
    }
}