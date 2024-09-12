package com.project.tictactoe.presentation.screen.history

import com.project.tictactoe.domain.model.History
import com.project.tictactoe.domain.usecase.DeleteAllHistoryUseCase
import com.project.tictactoe.domain.usecase.DeleteHistoryItemUseCase
import com.project.tictactoe.domain.usecase.GetHistoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class HistoryViewModelTest {

    private lateinit var viewModel: HistoryViewModel
    private val getHistoryUseCase: GetHistoryUseCase = mockk()
    private val deleteHistoryItemUseCase: DeleteHistoryItemUseCase = mockk(relaxed = true)
    private val deleteAllHistoryUseCase: DeleteAllHistoryUseCase = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val historyList = listOf(
        History(1, Date(), "Player 1", 0, "X", "O", "Player 2", opponentScore = 0),
        History(2, Date(), "Player 3", 0, "O", "X", "Player 4", opponentScore = 0)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HistoryViewModel(
            testDispatcher,
            getHistoryUseCase,
            deleteHistoryItemUseCase,
            deleteAllHistoryUseCase
        )


        coEvery { getHistoryUseCase() } returns historyList
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadHistory updates historyState`() = runTest {

        viewModel.handleEvent(HistoryEvent.LoadHistory)

        assertEquals(historyList.reversed(), viewModel.historyState.value)
    }

    @Test
    fun `handleEvent RemoveAllClicked calls deleteAllHistoryUseCase and loadHistory`() = runTest {
        viewModel.handleEvent(HistoryEvent.RemoveAllClicked)

        coVerify { deleteAllHistoryUseCase() }
        coVerify { getHistoryUseCase() }
    }

    @Test
    fun `handleEvent RemoveItemSwiped calls deleteHistoryItemUseCase and loadHistory`() = runTest {
        val historyItem = History(1, Date(), "Player 1", 0, "X", "O", "Player 2", 1)
        viewModel.handleEvent(HistoryEvent.RemoveItemSwiped(historyItem))

        coVerify { deleteHistoryItemUseCase(historyItem.uid) }
        coVerify { getHistoryUseCase() }
    }
}