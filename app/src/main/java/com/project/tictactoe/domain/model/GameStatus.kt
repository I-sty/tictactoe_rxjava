package com.project.tictactoe.domain.model

enum class GameStatus {
    NOT_STARTED,
    STARTED,
    IN_PROGRESS,
    PAUSED,
    ENDED_WITH_WINNER,
    ENDED_WITHOUT_WINNER,
}