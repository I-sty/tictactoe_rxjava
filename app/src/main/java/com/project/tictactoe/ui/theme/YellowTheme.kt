package com.project.tictactoe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkYellowColorPalette = darkColorScheme(
    primary = Yellow200,
    primaryContainer = Yellow700,
    secondary = Orange200,
    onPrimary = Color.White,
    onSecondary = Color.Black
)

private val LightYellowColorPalette = lightColorScheme(
    primary = Yellow500,
    primaryContainer = Yellow700,
    secondary = Orange200,
    onPrimary = Color.White,
    onSecondary = Color.Black

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun YellowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) DarkYellowColorPalette else LightYellowColorPalette
    TicTacToeGameTheme(colors, content)
}