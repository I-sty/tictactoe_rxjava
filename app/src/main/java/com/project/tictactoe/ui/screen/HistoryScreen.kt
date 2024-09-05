package com.project.tictactoe.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HistoryScreen(modifier: Modifier) {
    Column(modifier = modifier) {
        Text("History Screen")
        //TODO ( local storage repository)
    }
}

@Preview
@Composable
private fun HistoryScreenPreview() {
    HistoryScreen(modifier = Modifier)
}