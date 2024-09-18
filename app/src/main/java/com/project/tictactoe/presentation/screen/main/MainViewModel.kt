package com.project.tictactoe.presentation.screen.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.tictactoe.domain.model.GameState
import com.project.tictactoe.domain.usecase.ChangePlayerNameUseCase
import com.project.tictactoe.domain.usecase.HandleCellClickUseCase
import com.project.tictactoe.domain.usecase.NewMatchUseCase
import com.project.tictactoe.domain.usecase.RestartGameUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val handleCellClickUseCase: HandleCellClickUseCase,
    private val restartGameUseCase: RestartGameUseCase,
    private val newMatchUseCase: NewMatchUseCase,
    private val changePlayerNameUseCase: ChangePlayerNameUseCase
) : ViewModel() {
    private val _state: MutableLiveData<GameState> = MutableLiveData(GameState())
    val state: LiveData<GameState> = _state

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun handleEvent(event: GameEvent) {
        when (event) {
            is GameEvent.CellClicked -> {
                val disposable =
                    handleCellClickUseCase(_state.value!!, event.row, event.col)
                        .observeOn(
                            AndroidSchedulers.mainThread()
                        ).subscribe(
                            {
                                _state.value = it
                            },
                            {
                                Log.e("MainViewModel", "Error", it)
                            }
                        )
                disposables.add(disposable)
            }

            is GameEvent.PlayerChanged -> _state.value =
                _state.value?.copy(currentPlayer = event.player)

            GameEvent.RestartClicked -> {
                val disposable =
                    restartGameUseCase(_state.value!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { _state.value = it }
                disposables.add(disposable)
            }

            GameEvent.ShowWinnerDialog -> {
                _state.value = _state.value?.copy(showWinnerPopup = true)
            }

            GameEvent.OnDismissWinnerDialogClicked -> {
                _state.value = _state.value?.copy(showWinnerPopup = false)
                newMatch()
            }

            is GameEvent.PlayerNameChanged -> {
                val disposable = changePlayerNameUseCase(
                    _state.value!!,
                    event.player1Name,
                    event.player2Name
                ).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    _state.value = it
                }
                disposables.add(disposable)
            }

            GameEvent.DrawGameDialog -> _state.value = _state.value?.copy(showDrawGamePopup = true)
            GameEvent.OnDismissDrawGameDialogClicked -> {
                _state.value =
                    _state.value?.copy(showDrawGamePopup = false)
                newMatch()
            }
        }
    }

    private fun newMatch() {
        val disposable =
            newMatchUseCase(_state.value!!).observeOn(AndroidSchedulers.mainThread())
                .subscribe { _state.value = it }
        disposables.add(disposable)
    }
}