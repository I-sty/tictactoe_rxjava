package com.project.tictactoe.ui

import com.project.tictactoe.domain.model.Player

sealed class GameEvent {
    data object StartClicked : GameEvent()
    data object GameLoaded : GameEvent()
    data object RestartClicked : GameEvent()
    data class CellClicked(val row: Int, val col: Int) : GameEvent()
    data class PlayerChanged(val player: Player) : GameEvent()
    data object ResumeClicked : GameEvent()
    data object ShowWinnerDialog : GameEvent()
    data object OnDismissWinnerDialogClicked : GameEvent()
}
