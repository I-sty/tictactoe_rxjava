package com.project.tictactoe.presentation.screen.main

import com.project.tictactoe.domain.model.Player

sealed class GameEvent {
    data object RestartClicked : GameEvent()
    data class CellClicked(val row: Int, val col: Int) : GameEvent()
    data class PlayerChanged(val player: Player) : GameEvent()
    data object ShowWinnerDialog : GameEvent()
    data object OnDismissWinnerDialogClicked : GameEvent()
}
