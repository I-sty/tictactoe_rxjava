package com.project.tictactoe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tictactoe.data.domain.Player
import com.project.tictactoe.ui.GameEvent
import com.project.tictactoe.ui.MainViewModel
import com.project.tictactoe.ui.TicTacToeGameState
import com.project.tictactoe.ui.theme.PurpleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel(), modifier: Modifier) {
    val state by viewModel.state.collectAsState()

    MainScreenContent(state, handleEvent = viewModel::handleEvent, modifier = modifier)
}

@Composable
private fun MainScreenContent(
    state: TicTacToeGameState,
    handleEvent: (GameEvent) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            state.loading ->
                CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.CenterHorizontally))

            state.error != null -> {
                Text(text = "Error: ${state.error}", color = Color.Red)
            }

            else -> {
                PlayerSwitcher(state.currentPlayer) { player ->
                    handleEvent(GameEvent.PlayerChanged(player))
                }
                Spacer(modifier = Modifier.height(16.dp))
                TicTacToeBoard(state.board) { row, col ->
                    handleEvent(
                        GameEvent.CellClicked(
                            row,
                            col
                        )
                    )
                }
                if (state.winner != null) {
                    Text(
                        "Winner: ${state.winner.name}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerSwitcher(currentPlayer: Player, onPlayerChange: (Player) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        PlayerButton(player = Player.X, isSelected = currentPlayer == Player.X) {
            onPlayerChange(
                Player.X
            )
        }
        PlayerButton(player = Player.O, isSelected = currentPlayer == Player.O) {
            onPlayerChange(
                Player.O
            )
        }
    }
}

@Composable
private fun PlayerButton(player: Player, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color.LightGray else Color.White
    Text(
        text = "Player ${player.name}",
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
            .background(backgroundColor)
            .border(1.dp, Color.Gray)
    )
}

@Composable
private fun TicTacToeBoard(board: Array<Array<Player>>, onCellClick: (Int, Int) -> Unit) {
    Column {
        for (row in 0..2) {
            Row {
                for (col in 0..2) {
                    Cell(board[row][col], onCellClick, row, col)
                }
            }
        }
    }
}

@Composable
private fun Cell(player: Player, onCellClick: (Int, Int) -> Unit, row: Int, col: Int) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .border(1.dp, Color.Black)
            .clickable { onCellClick(row, col) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = player.symbol, style = MaterialTheme.typography.titleMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenContentPreview() {
    PurpleTheme {
        MainScreenContent(
            state = TicTacToeGameState().copy(winner = Player.O),
            handleEvent = {},
            modifier = Modifier
        )
    }
}
