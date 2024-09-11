package com.project.tictactoe.domain.model

enum class Player(val username: String, var score: Int = 0, val symbol: String) {
    X(username = "Player1", symbol = "✕"),
    O(username = "Player2", symbol = "○"),
    None(username = "", symbol = "")
}