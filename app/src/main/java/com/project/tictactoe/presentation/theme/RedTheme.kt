package com.project.tictactoe.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkRedColorPalette = darkColorScheme(
    primary = Red200,
    primaryContainer = Red700,
    secondary = Teal200,
    onPrimary = Color.White,
    onSecondary = Color.Black
)

private val LightRedColorPalette = lightColorScheme(
    primary = Red500,
    primaryContainer = Red700,
    secondary = Blue200,
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
fun RedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) DarkRedColorPalette else LightRedColorPalette
    TicTacToeGameTheme(colors, content)
}