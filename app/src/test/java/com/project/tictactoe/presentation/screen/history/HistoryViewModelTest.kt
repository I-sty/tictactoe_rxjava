package com.project.tictactoe.presentation.screen.history

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.tictactoe.domain.model.History
import com.project.tictactoe.domain.usecase.DeleteAllHistoryUseCase
import com.project.tictactoe.domain.usecase.DeleteHistoryItemUseCase
import com.project.tictactoe.domain.usecase.GetHistoryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

class HistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getHistoryUseCase: GetHistoryUseCase
    private lateinit var deleteHistoryItemUseCase: DeleteHistoryItemUseCase
    private lateinit var deleteAllHistoryUseCase: DeleteAllHistoryUseCase
    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setup() {
        getHistoryUseCase = mockk()
        deleteHistoryItemUseCase = mockk()
        deleteAllHistoryUseCase = mockk()
        viewModel = HistoryViewModel(
            getHistoryUseCase,
            deleteHistoryItemUseCase,
            deleteAllHistoryUseCase
        )
    }

    @Test
    fun `loadHistory should update historyLiveData`() {
        // Arrange
        val mockHistoryList = listOf(
            History(1, Date(), "Player 1", 1, "X", "O", "Player 2", 0)
        )
        coEvery { getHistoryUseCase() } returns Observable.just(mockHistoryList)

        // Act
        viewModel.handleEvent(HistoryEvent.LoadHistory)

        // Assert
        assertEquals(mockHistoryList, viewModel.getHistoryLiveData().value)
    }

    @Test
    fun `removeAllClicked should clear historyLiveData`() {
        // Arrange
        coEvery { deleteAllHistoryUseCase() } returns Observable.just(1)
        coEvery { getHistoryUseCase() } returns Observable.just(emptyList())

        // Act
        viewModel.handleEvent(HistoryEvent.RemoveAllClicked)

        // Assert
        assertEquals(emptyList<History>(), viewModel.getHistoryLiveData().value)
    }

    @Test
    fun `removeItemSwiped should remove item and update historyLiveData`() {
        // Arrange
        val historyItem = History(1, Date(), "Player 1", 1, "X", "O", "Player 2", 0)
        coEvery { deleteHistoryItemUseCase(historyItem.uid) } returns Observable.just(1)
        coEvery { getHistoryUseCase() } returns Observable.just(emptyList())

        // Act
        viewModel.handleEvent(HistoryEvent.RemoveItemSwiped(historyItem))

        // Assert
        assertEquals(emptyList<History>(), viewModel.getHistoryLiveData().value)
    }
}