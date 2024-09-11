package com.project.tictactoe.ui.screen.history

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.tictactoe.R
import com.project.tictactoe.domain.model.History
import com.project.tictactoe.domain.model.toHistoryString
import com.project.tictactoe.ui.common.TopAppBar
import com.project.tictactoe.ui.theme.PurpleTheme
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = koinViewModel(),
    navController: NavHostController,
    modifier: Modifier,
) {
    val historyState by viewModel.historyState.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        ConfirmDialog(title = stringResource(R.string.dialog_title_remove_all),
            message = stringResource(R.string.dialog_content_remove_all),
            onConfirm = {
                viewModel.handleEvent(HistoryEvent.RemoveAllClicked)
                showConfirmDialog = false
            },
            onDismiss = { showConfirmDialog = false }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(HistoryEvent.LoadHistory)
    }

    PurpleTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = stringResource(R.string.screen_title_history),
                    navController = navController
                )
            },
            floatingActionButton = { RemoveAllFab { showConfirmDialog = true } }
        ) { innerPadding ->
            HistoryScreenContent(
                historyState,
                modifier.padding(innerPadding),
                viewModel::handleEvent
            )
        }
    }
}

@Composable
private fun HistoryScreenContent(
    historyList: List<History>,
    modifier: Modifier,
    handleEvent: (HistoryEvent) -> Unit
) {
    if (historyList.isEmpty()) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.label_history_empty),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(top = 85.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(historyList) { history ->
                HistoryItemCard(
                    historyItem = history,
                    onRemoveCallback = { handleEvent(HistoryEvent.RemoveItemSwiped(history)) }
                )
            }
        }
    }
}

@Composable
private fun HistoryItemCard(
    historyItem: History,
    onRemoveCallback: () -> Unit
) {
    val context = LocalContext.current

    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onRemoveCallback()
                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }
    )

    val color = when (swipeState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart ->
            MaterialTheme.colorScheme.errorContainer

        else -> Color.Transparent
    }

    SwipeToDismissBox(
        state = swipeState,
        enableDismissFromStartToEnd = false,
        modifier = Modifier.animateContentSize(),
        backgroundContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        color,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete Icon",
                    modifier = Modifier.minimumInteractiveComponentSize(),
                )
            }
        },
        content = {
            HistoryItem(historyItem)
        }
    )
}

@Composable
private fun HistoryItem(historyItem: History) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(historyItem.playerName, style = MaterialTheme.typography.titleLarge)
                Text("vs", style = MaterialTheme.typography.bodySmall)
                Text(historyItem.opponentName, style = MaterialTheme.typography.titleLarge)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${historyItem.playerScore} - ${historyItem.opponentScore}",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    historyItem.timestamp.toHistoryString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun RemoveAllFab(onRemoveAll: () -> Unit) {
    FloatingActionButton(
        onClick = onRemoveAll,
        containerColor = MaterialTheme.colorScheme.errorContainer
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Remove All",
            tint = Color.White
        )
    }
}

@Composable
private fun ConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onConfirm) { Text(stringResource(R.string.label_confirm)) }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text(stringResource(android.R.string.cancel)) }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    val mockHistory = listOf(
        History(
            uid = 0,
            timestamp = Date(),
            playerName = "Player 1",
            playerScore = 1,
            playerSymbol = "X",
            opponentName = "Player 2",
            opponentScore = 0,
            opponentSymbol = "O"
        ),
        History(
            uid = 1,
            timestamp = Date(),
            playerName = "Alice",
            playerScore = 2,
            playerSymbol = "X",
            opponentName = "Bob",
            opponentScore = 1,
            opponentSymbol = "O"
        ),
        History(
            uid = 2,
            timestamp = Date(),
            playerName = "Charlie",
            playerScore = 0,
            playerSymbol = "O",
            opponentName = "David",
            opponentScore = 1,
            opponentSymbol = "X"
        )
    )
    PurpleTheme {
        HistoryScreenContent(historyList = mockHistory, modifier = Modifier, handleEvent = {})
    }
}