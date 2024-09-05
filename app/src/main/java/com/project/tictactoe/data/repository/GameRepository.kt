package com.project.tictactoe.data.repository

import com.project.tictactoe.data.domain.Player
import kotlinx.coroutines.delay
import org.koin.core.annotation.Single

interface IGameRepository {
    suspend fun getGame(): String
    suspend fun getPlayers(): List<Player>
}

@Single(binds = [IGameRepository::class])
class GameRepository : IGameRepository {
    override suspend fun getGame(): String {
        delay(1000)
        return "Tic-Tac-Toe game"
    }

    override suspend fun getPlayers(): List<Player> {
        delay(500)
        return emptyList()
    }
}