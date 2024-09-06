package com.project.tictactoe.ui

import com.project.tictactoe.domain.model.Player

sealed class GameEvent {
    data object StartClicked : GameEvent()
    data object MenuClicked : GameEvent()
    data object GameLoaded : GameEvent()
    data class CellClicked(val row: Int, val col: Int) : GameEvent()
    data class PlayerChanged(val player: Player) : GameEvent()
}
