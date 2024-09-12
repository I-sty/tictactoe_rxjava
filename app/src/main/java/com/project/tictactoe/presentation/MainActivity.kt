package com.project.tictactoe.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.tictactoe.presentation.screen.about.AboutScreen
import com.project.tictactoe.presentation.screen.history.HistoryScreen
import com.project.tictactoe.presentation.screen.main.MainScreen
import com.project.tictactoe.presentation.screen.nameeditor.NamePickerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()
            navController.enableOnBackPressed(true)

            val modifier = Modifier.fillMaxSize()

            NavHost(navController = navController, startDestination = "namePicker") {
                composable("main/{player1Name}/{player2Name}") {
                    MainScreen(
                        navController = navController,
                        modifier = modifier
                    )
                }
                composable("about") {
                    AboutScreen(
                        navController = navController,
                        modifier = modifier
                    )
                }
                composable("history") {
                    HistoryScreen(
                        navController = navController,
                        modifier = modifier
                    )
                }
                composable("namePicker") {
                    NamePickerScreen(
                        navController = navController,
                        modifier = modifier
                    )
                }
            }
        }
    }
}