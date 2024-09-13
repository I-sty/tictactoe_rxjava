package com.project.tictactoe.presentation.screen.nameeditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.project.tictactoe.R
import com.project.tictactoe.presentation.common.MainScreenRoute
import com.project.tictactoe.presentation.common.TopAppBar
import com.project.tictactoe.presentation.theme.PurpleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NamePickerScreen(
    modifier: Modifier = Modifier,
    viewModel: NamePickerViewModel = koinViewModel(),
    navController: NavHostController,
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = viewModel) {
        viewModel.navigation.collect {
            when (it) {
                is NamePickerNavigation.NavigateToGameScreen -> {
                    val player1Name = it.player1Name
                    val player2Name = it.player2Name

                    navController.navigate(
                        route = MainScreenRoute(player1Name, player2Name),
                        navOptions = NavOptions.Builder().setPopUpTo("namePicker", true)
                            .build()
                    )
                }
            }
        }
    }

    PurpleTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = stringResource(R.string.title_name_editor),
                navController = navController,
                canNavigateBack = false,
                showActions = false
            )
        }) {
            NamePickerScreenContent(
                state = state,
                onEvent = viewModel::onEvent,
                modifier = modifier
                    .consumeWindowInsets(it)
                    .padding(it)
            )
        }
    }
}

@Composable
private fun NamePickerScreenContent(
    state: NamePickerViewState,
    onEvent: (NamePickerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.player1Name,
            onValueChange = { onEvent(NamePickerEvent.OnPlayer1NameChanged(it)) },
            label = { Text("Player 1 Name") },
            placeholder = { Text("Enter player 1 name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = state.player2Name,
            onValueChange = { onEvent(NamePickerEvent.OnPlayer2NameChanged(it)) },
            label = { Text("Player 2 Name") },
            placeholder = { Text("Enter player 2 name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { onEvent(NamePickerEvent.OnConfirmClicked) },
                enabled = state.isConfirmButtonEnabled
            ) {
                Text("Confirm")
            }

            Button(onClick = { onEvent(NamePickerEvent.OnSkipClicked) }) {
                Text("Skip")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NamePickerScreenPreview() {
    val mockState = NamePickerViewState(
        player1Name = "John",
        player2Name = "Jane",
    )
    PurpleTheme {
        NamePickerScreenContent(mockState, {}, Modifier)
    }

}