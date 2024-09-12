package com.project.tictactoe.domain.model

enum class Player(val username: String, var score: Int, val symbol: String) {
    X(username = "Player1", symbol = "✕", score = 0),
    O(username = "Player2", symbol = "○", score = 0),
    None(username = "", symbol = "", score = 0)
}