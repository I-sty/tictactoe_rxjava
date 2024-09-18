package com.project.tictactoe.presentation.screen.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.tictactoe.domain.model.GameState
import com.project.tictactoe.domain.model.GameStatus
import com.project.tictactoe.domain.model.Player
import com.project.tictactoe.domain.usecase.ChangePlayerNameUseCase
import com.project.tictactoe.domain.usecase.HandleCellClickUseCase
import com.project.tictactoe.domain.usecase.NewMatchUseCase
import com.project.tictactoe.domain.usecase.RestartGameUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val handleCellClickUseCase: HandleCellClickUseCase = mockk(relaxed = true)
    private val restartGameUseCase: RestartGameUseCase = mockk(relaxed = true)
    private val newMatchUseCase: NewMatchUseCase = mockk(relaxed = true)
    private val changePlayerNameUseCase: ChangePlayerNameUseCase = mockk(relaxed = true)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = MainViewModel(
            handleCellClickUseCase,
            restartGameUseCase,
            newMatchUseCase,
            changePlayerNameUseCase
        )
    }

    @Test
    fun `handleEvent CellClicked updates board and current player`() {
        // Arrange
        val initialState = viewModel.state.value

        // Act
        viewModel.handleEvent(GameEvent.CellClicked(0, 0))

        // Assert
        val updatedState = viewModel.state.value ?: return
        assertEquals(Player.X, updatedState.board[0][0])
        assertEquals(Player.O, updatedState.currentPlayer)
        assertEquals(initialState?.status, updatedState.status)
    }

    @Test
    fun `handleEvent CellClicked detects winner and updates score`() {
        // Arrange
        viewModel.handleEvent(GameEvent.RestartClicked)

        // Act
        viewModel.handleEvent(GameEvent.CellClicked(0, 0))
        viewModel.handleEvent(GameEvent.CellClicked(1, 0))
        viewModel.handleEvent(GameEvent.CellClicked(0, 1))
        viewModel.handleEvent(GameEvent.CellClicked(1, 1))
        viewModel.handleEvent(GameEvent.CellClicked(0, 2))

        // Assert
        val updatedState = viewModel.state.value ?: return
        assertEquals(Player.X, updatedState.winner)
        assertEquals(1, updatedState.playerX.score)
        assertEquals(GameStatus.ENDED_WITH_WINNER, updatedState.status)
//        coVerify { handleCellClickUseCase(updatedState.playerX, updatedState.playerO) }
    }

    @Test
    fun `handleEvent CellClicked detects draw and updates status`() {
        // Arrange
        viewModel.handleEvent(GameEvent.CellClicked(0, 0))
        viewModel.handleEvent(GameEvent.CellClicked(1, 0))
        viewModel.handleEvent(GameEvent.CellClicked(2, 0))
        viewModel.handleEvent(GameEvent.CellClicked(0, 1))
        viewModel.handleEvent(GameEvent.CellClicked(1, 1))
        viewModel.handleEvent(GameEvent.CellClicked(0, 2))
        viewModel.handleEvent(GameEvent.CellClicked(1, 2))
        viewModel.handleEvent(GameEvent.CellClicked(2, 1))

        // Act
        viewModel.handleEvent(GameEvent.CellClicked(2, 2))

        // Assert
        val updatedState = viewModel.state.value ?: return
        assertEquals(Player.None, updatedState.winner)
        assertEquals(GameStatus.ENDED_WITHOUT_WINNER, updatedState.status)
//        coVerify { addHistoryUseCase(updatedState.playerX, updatedState.playerO) }
    }

    @Test
    fun `handleEvent RestartClicked resets game state`() {
        // Arrange
        viewModel.handleEvent(GameEvent.CellClicked(0, 0))
        viewModel.handleEvent(GameEvent.CellClicked(1, 0))

        // Act
        viewModel.handleEvent(GameEvent.RestartClicked)

        // Assert
        val updatedState = viewModel.state.value ?: return
        assertEquals(Player.X, updatedState.currentPlayer)
        assertEquals(null, updatedState.winner)
        assertEquals(GameStatus.NOT_STARTED, updatedState.status)
        assertEquals(0, updatedState.playerX.score)
        assertEquals(0, updatedState.playerO.score)
        checkBoard(updatedState)
    }

    @Test
    fun `handleEvent PlayerChanged updates current player`() {
        // Arrange
        viewModel.handleEvent(GameEvent.RestartClicked)

        // Act
        viewModel.handleEvent(GameEvent.PlayerChanged(Player.O))

        // Assert
        val updatedState = viewModel.state.value ?: return
        assertEquals(Player.O, updatedState.currentPlayer)
    }

    @Test
    fun `restartGame resets game state with correct winner`() {
        // Arrange
        viewModel.handleEvent(GameEvent.CellClicked(0, 0))
        viewModel.handleEvent(GameEvent.CellClicked(1, 0))
        viewModel.handleEvent(GameEvent.CellClicked(0, 1))
        viewModel.handleEvent(GameEvent.CellClicked(1, 1))
        viewModel.handleEvent(GameEvent.CellClicked(0, 2))

        // Act
        viewModel.handleEvent(GameEvent.RestartClicked)

        // Assert
        val updatedState = viewModel.state.value ?: return
        assertEquals(Player.X, updatedState.currentPlayer)
        assertEquals(null, updatedState.winner)
        assertEquals(GameStatus.NOT_STARTED, updatedState.status)
        checkBoard(updatedState)
    }

    @Test
    fun `handleEvent ShowWinnerDialog sets showWinnerPopup to true`() {
        // Arrange
        viewModel.handleEvent(GameEvent.RestartClicked)

        // Act
        viewModel.handleEvent(GameEvent.ShowWinnerDialog)

        // Assert
        val updatedState = viewModel.state.value ?: return
        assertEquals(true, updatedState.showWinnerPopup)
    }

    @Test
    fun `handleEvent OnDismissWinnerDialogClicked sets showWinnerPopup to false and calls newGame`() {
        // Arrange
        val customViewModel: MainViewModel = spyk(viewModel)


        val previousState = GameState(
            status = GameStatus.ENDED_WITH_WINNER,
            winner = Player.X
        )
        coEvery { customViewModel.state.value } returns previousState

        // Act
        customViewModel.handleEvent(GameEvent.OnDismissWinnerDialogClicked)

        // Assert
        val updatedState = customViewModel.state.value ?: return
        assertEquals(false, updatedState.showWinnerPopup)
        assertEquals(GameStatus.NOT_STARTED, updatedState.status)
        assertEquals(null, updatedState.winner)
        checkBoard(updatedState)
    }

    private fun checkBoard(updatedState: GameState?) {
        if (updatedState == null) return
        for (row in updatedState.board) {
            for (cell in row) {
                assertEquals(Player.None, cell)
            }
        }
    }
}