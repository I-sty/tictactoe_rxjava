package com.project.tictactoe.presentation.screen.nameeditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NamePickerViewModel : ViewModel() {
    private val _state = MutableStateFlow(NamePickerViewState())
    val uiState: StateFlow<NamePickerViewState> = _state.asStateFlow()

    private val _navigation = Channel<NamePickerNavigation>(Channel.CONFLATED)
    val navigation = _navigation.receiveAsFlow()

    fun onEvent(event: NamePickerEvent) {
        when (event) {
            is NamePickerEvent.OnPlayer1NameChanged -> {
                _state.value = _state.value.copy(player1Name = event.name)
                _state.value = _state.value.copy(isConfirmButtonEnabled = validateNames())
            }

            is NamePickerEvent.OnPlayer2NameChanged -> {
                _state.value = _state.value.copy(player2Name = event.name)
                _state.value = _state.value.copy(isConfirmButtonEnabled = validateNames())
            }

            is NamePickerEvent.OnConfirmClicked -> {
                viewModelScope.launch {
                    _navigation.send(
                        NamePickerNavigation.NavigateToGameScreen(
                            player1Name = _state.value.player1Name,
                            player2Name = _state.value.player2Name
                        )
                    )
                }
            }

            is NamePickerEvent.OnSkipClicked -> {
                viewModelScope.launch {
                    _navigation.send(
                        NamePickerNavigation.NavigateToGameScreen(
                            player1Name = "John",
                            player2Name = "Jane"
                        )
                    )
                }
            }
        }
    }

    private fun validateNames(): Boolean {
        return with(_state.value) {
            player1Name.isNotBlank() && player2Name.isNotBlank() && player1Name != player2Name
        }
    }
}

data class NamePickerViewState(
    val player1Name: String = "",
    val player2Name: String = "",
    val isConfirmButtonEnabled: Boolean = false
)

sealed class NamePickerEvent {
    data class OnPlayer1NameChanged(val name: String) : NamePickerEvent()
    data class OnPlayer2NameChanged(val name: String) : NamePickerEvent()
    data object OnConfirmClicked : NamePickerEvent()
    data object OnSkipClicked : NamePickerEvent()
}

sealed class NamePickerNavigation {
    data class NavigateToGameScreen(val player1Name: String, val player2Name: String) :
        NamePickerNavigation()
}