package com.project.tictactoe.ui.screen

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.tictactoe.ui.common.TicTacToeTopAppBar

@Composable
fun TicTacToeScreen() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TicTacToeTopAppBar(navController) }
    ) { innerPadding ->
        Navigation(
            navController,
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(modifier = modifier) }
        composable("about") { AboutScreen(modifier) }
        composable("history") { HistoryScreen(modifier) }
    }
}
