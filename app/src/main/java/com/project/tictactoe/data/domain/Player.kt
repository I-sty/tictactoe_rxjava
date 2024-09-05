package com.project.tictactoe.data.domain

enum class Player(val username: String, var score: Int = 0, val symbol: String) {
    X(username = "Player1", symbol = "X"),
    O(username = "Player2", symbol = "O"),
    None(username = "", symbol = "")
}