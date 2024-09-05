package com.project.tictactoe

import kotlin.system.exitProcess

data class ConsolePlayer(val name: String, var score: Int = 0)

class TicTacToe(private val player1: ConsolePlayer, private val player2: ConsolePlayer) {
    private val board = Array(3) { CharArray(3) { ' ' } }
    private var currentPlayer = player1
    private var gameWon = false

    private fun printBoard() {
        println("Current Board:")
        for (row in board) {
            println(row.joinToString(" | ") { if (it == ' ') "_" else it.toString() })
        }
    }

    private fun checkWin(): Boolean {
        val winConditions = listOf(
            // Rows
            listOf(board[0][0], board[0][1], board[0][2]),
            listOf(board[1][0], board[1][1], board[1][2]),
            listOf(board[2][0], board[2][1], board[2][2]),
            // Columns
            listOf(board[0][0], board[1][0], board[2][0]),
            listOf(board[0][1], board[1][1], board[2][1]),
            listOf(board[0][2], board[1][2], board[2][2]),
            // Diagonals
            listOf(board[0][0], board[1][1], board[2][2]),
            listOf(board[0][2], board[1][1], board[2][0])
        )
        return winConditions.any { it.all { cell -> cell == currentPlayerSymbol() } }
    }

    private fun isDraw(): Boolean {
        return board.all { row -> row.all { cell -> cell != ' ' } }
    }

    private fun currentPlayerSymbol() = if (currentPlayer == player1) 'X' else 'O'

    private fun switchPlayer() {
        currentPlayer = if (currentPlayer == player1) player2 else player1
    }

    private fun resetBoard() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                board[i][j] = ' '
            }
        }
        gameWon = false
        currentPlayer = player1
    }

    fun play() {
        resetBoard()
        while (true) {
            printBoard()
            println("${currentPlayer.name}'s turn (playing '${currentPlayerSymbol()}'). Enter position (1-9):")
            val input = readlnOrNull()
            if (input == "m") {
                showMenu()
                if (gameWon) return
            } else {
                val position = input?.toIntOrNull()
                if (position != null && position in 1..9 && makeMove(position)) {
                    if (checkWin()) {
                        gameWon = true
                        printBoard()
                        println("Congratulations, ${currentPlayer.name} wins!")
                        currentPlayer.score++
                        showMenu(false)
                        return
                    } else if (isDraw()) {
                        printBoard()
                        println("It's a draw!")
                        showMenu(false)
                        return
                    }
                    switchPlayer()
                } else {
                    println("Invalid input, please try again.")
                }
            }
        }
    }

    private fun makeMove(position: Int): Boolean {
        val row = (position - 1) / 3
        val col = (position - 1) % 3
        if (board[row][col] == ' ') {
            board[row][col] = currentPlayerSymbol()
            return true
        }
        return false
    }

    private fun showMenu(showBackOption: Boolean = true) {
        while (true) {
            println("Menu:")
            if (showBackOption) println("1. Back")
            println("2. Score")
            println("3. New Game")
            println("4. About")
            println("5. Exit Game")
            when (readlnOrNull()?.toIntOrNull()) {
                1 -> if (showBackOption) return
                2 -> showScore()
                3 -> {
                    resetBoard()
                    return
                }

                4 -> showAbout()
                5 -> exitProcess(0)
                else -> println("Invalid option, please try again.")
            }
        }
    }

    private fun showScore() {
        println("Score:")
        println("${player1.name}: ${player1.score} | ${player2.name}: ${player2.score}")
    }

    private fun showAbout() {
        println("Tic-Tac-Toe Game")
    }
}

fun main() {
    println("Enter name for Player 1:")
    var player1Name = readlnOrNull()
    if (player1Name.isNullOrEmpty()) {
        player1Name = " Player 1"
    }

    println("Enter name for Player 2:")
    var player2Name = readlnOrNull()
    if (player2Name.isNullOrEmpty()) {
        player2Name = " Player 2"
    }

    val player1 = ConsolePlayer(player1Name)
    val player2 = ConsolePlayer(player2Name)

    val game = TicTacToe(player1, player2)

    while (true) {
        game.play()
    }
}
