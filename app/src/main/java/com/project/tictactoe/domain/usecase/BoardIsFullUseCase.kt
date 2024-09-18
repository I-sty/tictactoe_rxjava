package com.project.tictactoe.domain.usecase

import com.project.tictactoe.domain.model.Player
import org.koin.core.annotation.Factory

@Factory
class BoardIsFullUseCase {

    operator fun invoke(board: Array<Array<Player>>): Boolean {
        return board.all { row -> row.none { it == Player.None } }
    }
}