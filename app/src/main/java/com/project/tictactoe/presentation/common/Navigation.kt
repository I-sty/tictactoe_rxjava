package com.project.tictactoe.presentation.common

import kotlinx.serialization.Serializable

@Serializable
data class MainScreenRoute(val player1Name: String, val player2Name: String)

@Serializable
object AboutScreenRoute

@Serializable
object HistoryScreenRoute

@Serializable
object NamePickerScreenRoute
