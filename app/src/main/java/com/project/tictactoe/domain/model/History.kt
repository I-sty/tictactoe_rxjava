package com.project.tictactoe.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class History(
    val timestamp: Date,
    val playerName: String,
    val playerScore: Int,
    val playerSymbol: String,
    val opponentSymbol: String,
    val opponentName: String,
    val opponentScore: Int
)

fun String.toDate(): Date {
    return SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss",
        Locale.getDefault()
    ).parse(this) ?: Date()
}