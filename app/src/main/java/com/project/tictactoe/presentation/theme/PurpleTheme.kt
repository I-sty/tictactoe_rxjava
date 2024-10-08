package com.project.tictactoe.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Purple200,
    primaryContainer = Purple700,
    secondary = Teal200,
    onSecondaryContainer = Teal200,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    surface = Purple200,
    errorContainer = Color.Red,
)

private val LightColorPalette = lightColorScheme(
    primary = Purple500,
    primaryContainer = Purple700,
    secondary = Teal200,
    onSecondaryContainer = Teal200,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    surface = Purple500,
    errorContainer = Color.Red,
)

@Composable
fun PurpleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    TicTacToeGameTheme(colors, content)
}