package com.project.tictactoe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val timestamp: String,
    val playerName: String,
    val playerScore: Int,
    val playerSymbol: String,
    val opponentSymbol: String,
    val opponentName: String,
    val opponentScore: Int
)
