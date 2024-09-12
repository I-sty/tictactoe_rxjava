package com.project.tictactoe.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

enum class ThemeType {
    PURPLE,
    RED,
    YELLOW
}

@Composable
fun TicTacToeGameTheme(
    colors: ColorScheme,
    content: @Composable() () -> Unit
) {

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
