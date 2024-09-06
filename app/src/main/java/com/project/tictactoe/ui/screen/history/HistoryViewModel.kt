package com.project.tictactoe.ui.screen.history

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HistoryViewModel : ViewModel() {
    val history = mutableListOf<String>()
}