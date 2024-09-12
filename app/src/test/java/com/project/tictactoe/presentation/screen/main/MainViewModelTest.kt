package com.project.tictactoe.presentation.screen.main

import com.project.tictactoe.domain.model.GameState
import com.project.tictactoe.domain.model.GameStatus
import com.project.tictactoe.domain.model.Player
import com.project.tictactoe.domain.usecase.AddHistoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val addHistoryUseCase: AddHistoryUseCase = mockk(relaxed = true)

    @Before
    fun setup() {
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(addHistoryUseCase, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `handleEvent CellClicked updates board and current player`() = runTest {
        val initialState = viewModel.state.value
        viewModel.handleEvent(GameEvent.CellClicked(0, 0))

        val updatedState = viewModel.state.value
        assertEquals(Player.X, updatedState.board[0][0])
        assertEquals(Player.O, updatedState.currentPlayer)
        assertEquals(initialState.status, updatedState.status)
    }

    @Test
    fun `handleEvent CellClicked detects winner and updates score`() = runTest {
        // Reset game
        viewModel.restartGame()

        // Set up board for X to win
        viewModel.handleEvent(GameEvent.CellClicked(0, 0))
        viewModel.handleEvent(GameEvent.CellClicked(1, 0))
        viewModel.handleEvent(GameEvent.CellClicked(0, 1))
        viewModel.handleEvent(GameEvent.CellClicked(1, 1))
        viewModel.handleEvent(GameEvent.CellClicked(0, 2))

        val updatedState = viewModel.state.value
        assertEquals(Player.X, updatedState.winner)
        assertEquals(1, updatedState.playerX.score)
        assertEquals(GameStatus.ENDED_WITH_WINNER, updatedState.status)
        coVerify { addHistoryUseCase(updatedState.playerX, updatedState.playerO) }
    }

    @Test
    fun `handleEvent CellClicked detects draw and updates status`() = runTest {
        // Set up board for a draw
        viewModel.handleEvent(GameEvent.CellClicked(0, 0))
        viewModel.handleEvent(GameEvent.CellClicked(1, 0))
        viewModel.handleEvent(GameEvent.CellClicked(2, 0))
        viewModel.handleEvent(GameEvent.CellClicked(0, 1))
        viewModel.handleEvent(GameEvent.CellClicked(1, 1))
        viewModel.handleEvent(GameEvent.CellClicked(0, 2))
        viewModel.handleEvent(GameEvent.CellClicked(1, 2))
        viewModel.handleEvent(GameEvent.CellClicked(2, 1))

        viewModel.handleEvent(GameEvent.CellClicked(2, 2))

        val updatedState = viewModel.state.value
        assertEquals(Player.None, updatedState.winner)
        assertEquals(GameStatus.ENDED_WITHOUT_WINNER, updatedState.status)
        coVerify { addHistoryUseCase(updatedState.playerX, updatedState.playerO) }
    }

    @Test
    fun `handleEvent RestartClicked resets game state`() = runTest {
        // Make some moves
        viewModel.handleEvent(GameEvent.CellClicked(0, 0))
        viewModel.handleEvent(GameEvent.CellClicked(1, 0))

        viewModel.handleEvent(GameEvent.RestartClicked)

        val updatedState = viewModel.state.value
        assertEquals(Player.X, updatedState.currentPlayer)
        assertEquals(null, updatedState.winner)
        assertEquals(GameStatus.NOT_STARTED, updatedState.status)
        assertEquals(0, updatedState.playerX.score)
        assertEquals(0, updatedState.playerO.score)

        // Check if the board is empty
        checkBoard(updatedState)
    }

    @Test
    fun `handleEvent PlayerChanged updates current player`() = runTest {
        viewModel.handleEvent(GameEvent.PlayerChanged(Player.O))

        val updatedState = viewModel.state.value
        assertEquals(Player.O, updatedState.currentPlayer)
    }

    @Test
    fun `restartGame resets game state with correct winner`() = runTest {
        // Set a winner
        viewModel.handleEvent(GameEvent.CellClicked(0, 0))
        viewModel.handleEvent(GameEvent.CellClicked(1, 0))
        viewModel.handleEvent(GameEvent.CellClicked(0, 1))
        viewModel.handleEvent(GameEvent.CellClicked(1, 1))
        viewModel.handleEvent(GameEvent.CellClicked(0, 2))

        viewModel.restartGame()

        val updatedState = viewModel.state.value
        assertEquals(Player.X, updatedState.currentPlayer)

        // Winner starts next game
        assertEquals(null, updatedState.winner)
        assertEquals(GameStatus.NOT_STARTED, updatedState.status)

        // Check if the board is empty
        checkBoard(updatedState)
    }

    @Test
    fun `handleEvent ShowWinnerDialog sets showWinnerPopup to true`() = runTest {
        viewModel.handleEvent(GameEvent.ShowWinnerDialog)

        val updatedState = viewModel.state.value
        assertEquals(true, updatedState.showWinnerPopup)
    }

    @Test
    fun `handleEvent OnDismissWinnerDialogClicked sets showWinnerPopup to false and calls newGame`() =
        runTest {
            val customViewModel: MainViewModel = spyk(viewModel)

            // Mock previous state to simulate a game ending
            val previousState = GameState(
                status = GameStatus.ENDED_WITH_WINNER,
                winner = Player.X
            )
            coEvery { customViewModel.state.value } returns previousState

            customViewModel.handleEvent(GameEvent.OnDismissWinnerDialogClicked)

            val updatedState = customViewModel.state.value
            assertEquals(false, updatedState.showWinnerPopup)
            assertEquals(GameStatus.NOT_STARTED, updatedState.status)
            assertEquals(null, updatedState.winner)

            // Check if the board is empty
            checkBoard(updatedState)
        }

    private fun checkBoard(updatedState: GameState) {
        for (row in updatedState.board) {
            for (cell in row) {
                assertEquals(Player.None, cell)
            }
        }
    }

}