package com.project.tictactoe.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tictactoe.domain.model.History
import com.project.tictactoe.domain.usecase.DeleteAllHistoryUseCase
import com.project.tictactoe.domain.usecase.DeleteHistoryItemUseCase
import com.project.tictactoe.domain.usecase.GetHistoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HistoryViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val deleteHistoryItemUseCase: DeleteHistoryItemUseCase,
    private val deleteAllHistoryUseCase: DeleteAllHistoryUseCase,
) : ViewModel() {
    private val _historyState = MutableStateFlow<List<History>>(emptyList())
    val historyState: StateFlow<List<History>> = _historyState.asStateFlow()

    private fun loadHistory() {
        viewModelScope.launch(ioDispatcher) {
            val history = getHistoryUseCase()
            _historyState.value = history.reversed()
        }
    }

    fun handleEvent(event: HistoryEvent) {
        when (event) {
            HistoryEvent.RemoveAllClicked -> {
                viewModelScope.launch(ioDispatcher) {
                    deleteAllHistoryUseCase()
                    loadHistory()
                }
            }

            is HistoryEvent.RemoveItemSwiped -> {
                viewModelScope.launch(ioDispatcher) {
                    deleteHistoryItemUseCase(event.history.uid)
                    loadHistory()
                }
            }

            HistoryEvent.LoadHistory -> loadHistory()
        }
    }
}

sealed class HistoryEvent {
    data object RemoveAllClicked : HistoryEvent()
    data class RemoveItemSwiped(val history: History) : HistoryEvent()
    data object LoadHistory : HistoryEvent()
}