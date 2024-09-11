package com.project.tictactoe.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class History(
    val uid: Int,
    val timestamp: Date,
    val playerName: String,
    val playerScore: Int,
    val playerSymbol: String,
    val opponentSymbol: String,
    val opponentName: String,
    val opponentScore: Int
)

fun String.toDAODate(): Date {
    return SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss",
        Locale.getDefault()
    ).parse(this) ?: Date()
}

fun Date.toDAOString(): String {
    return SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss",
        Locale.getDefault()
    ).format(this)
}

fun Date.toHistoryString(): String {
    return SimpleDateFormat(
        "yyyy MMM dd - HH:mm:ss",
        Locale.getDefault()
    ).format(this)
}