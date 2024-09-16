package com.project.tictactoe.presentation.screen.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.tictactoe.domain.model.History
import com.project.tictactoe.domain.usecase.DeleteAllHistoryUseCase
import com.project.tictactoe.domain.usecase.DeleteHistoryItemUseCase
import com.project.tictactoe.domain.usecase.GetHistoryUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HistoryViewModel(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val deleteHistoryItemUseCase: DeleteHistoryItemUseCase,
    private val deleteAllHistoryUseCase: DeleteAllHistoryUseCase,
) : ViewModel() {

    companion object {
        private val TAG = "HistoryViewModel"
    }

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val historyLiveData: MutableLiveData<List<History>> = MutableLiveData()

    fun getHistoryLiveData(): LiveData<List<History>> {
        return historyLiveData
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun loadHistory() {
        val disposable: Disposable = getHistoryUseCase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { historyList -> historyLiveData.setValue(historyList) },
                { throwable -> Log.e(TAG, "Error loading history", throwable) }
            )
        disposables.add(disposable)
    }

    fun handleEvent(event: HistoryEvent) {
        when (event) {
            HistoryEvent.RemoveAllClicked -> {
                val disposable =
                    deleteAllHistoryUseCase()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { numberOfRows ->
                                Log.i(
                                    TAG,
                                    "handleEvent: Deleted rows: $numberOfRows"
                                )
                            },
                            { throwable -> Log.e(TAG, "Error delete all history", throwable) }
                        )
                disposables.add(disposable)
                loadHistory()
            }

            is HistoryEvent.RemoveItemSwiped -> {
                val disposable =
                    deleteHistoryItemUseCase(event.history.uid)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { numberOfRows ->
                                Log.i(
                                    TAG,
                                    "handleEvent: Deleted rows: $numberOfRows"
                                )
                            },
                            { throwable -> Log.e(TAG, "Error delete history item", throwable) }
                        )
                disposables.add(disposable)
                loadHistory()
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