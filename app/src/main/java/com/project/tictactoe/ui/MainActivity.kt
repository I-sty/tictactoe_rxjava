package com.project.tictactoe.ui

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
import com.project.tictactoe.ui.screen.AboutScreen
import com.project.tictactoe.ui.screen.MainScreen
import com.project.tictactoe.ui.screen.history.HistoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()
            navController.enableOnBackPressed(true)

            val modifier = Modifier.fillMaxSize()

            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
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
            }
        }
    }
}