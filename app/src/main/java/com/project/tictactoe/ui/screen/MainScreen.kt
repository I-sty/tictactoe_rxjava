package com.project.tictactoe.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
private fun PlayerButton(
    player: Player,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = CircleShape
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp)
            .size(128.dp),
        shape = shape,
        border = BorderStroke(
            width = 2.dp,
            color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.3f
            )
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.3f
            )
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = when (player) {
                    Player.X -> "✕"
                    Player.O -> "○"
                    else -> ""
                },
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Player ${player.name}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Score: ${player.score}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun TicTacToeBoard(board: Array<Array<Player>>, onCellClick: (Int, Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        for (row in 0..2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (col in 0..2) {
                    Cell(
                        player = board[row][col],
                        onClick = { onCellClick(row, col) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Cell(player: Player, onClick: () -> Unit) {
    val color = when (player) {
        Player.X -> MaterialTheme.colorScheme.primary
        Player.O -> MaterialTheme.colorScheme.secondary
        Player.None -> Color.Transparent
    }
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp)
            .clickable(onClick = onClick)
            .border(
                2.dp,
                MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (player != Player.None) {
            Text(
                text = when (player) {
                    Player.X -> "✕"
                    Player.O -> "○"
                    else -> ""
                },
                style = MaterialTheme.typography.displayMedium,
                color = color
            )
        }
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
