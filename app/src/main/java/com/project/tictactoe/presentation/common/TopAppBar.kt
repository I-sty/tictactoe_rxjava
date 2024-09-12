package com.project.tictactoe.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.project.tictactoe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String = stringResource(R.string.app_name),
    navController: NavHostController,
    canNavigateBack: Boolean = true,
    showActions: Boolean = true
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors()
            .copy(
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
                navigationIconContentColor = Color.White
            ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (showActions) {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "More"
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.screen_title_history)) },
                        onClick = {
                            navController.navigate("history")
                            showMenu = false
                        })
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.screen_title_about)) },
                        onClick = {
                            navController.navigate("about")
                            showMenu = false
                        })
                }
            }
        }
    )
}