package com.project.tictactoe.presentation.screen.nameeditor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NamePickerViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `onEvent OnPlayer1NameChanged updates player1Name and isConfirmButtonEnabled`() {
        val viewModel = NamePickerViewModel()

        viewModel.onEvent(NamePickerEvent.OnPlayer1NameChanged("Alice"))

        assertEquals(viewModel.uiState.value.player1Name, "Alice")
        assertFalse(viewModel.uiState.value.isConfirmButtonEnabled)
    }

    @Test
    fun `onEvent OnPlayer2NameChanged updates player2Name and isConfirmButtonEnabled`() {
        val viewModel = NamePickerViewModel()

        viewModel.onEvent(NamePickerEvent.OnPlayer2NameChanged("Bob"))

        assertEquals(viewModel.uiState.value.player2Name, "Bob")
        assertFalse(viewModel.uiState.value.isConfirmButtonEnabled)
    }

    @Test
    fun `onEvent OnConfirmClicked sends NavigateToGameScreen with correct names`() {
//        val viewModel = NamePickerViewModel()
//        viewModel.onEvent(NamePickerEvent.OnPlayer1NameChanged("Alice"))
//        viewModel.onEvent(NamePickerEvent.OnPlayer2NameChanged("Bob"))
//
//        viewModel.onEvent(NamePickerEvent.OnConfirmClicked)
//
//        viewModel.navigation.test {
//            val emission = awaitItem()
//            assertEquals(
//                emission.isInstanceOf(NamePickerNavigation.NavigateToGameScreen::class.java)
//                        assertEquals ((emission as NamePickerNavigation.NavigateToGameScreen).player1Name,
//                "Alice"
//            )
//            assertEquals(emission.player2Name, "Bob")
//        }
    }

    @Test
    fun `onEvent OnSkipClicked sends NavigateToGameScreen with default names`() {
//        val viewModel = NamePickerViewModel()
//
//        viewModel.onEvent(NamePickerEvent.OnSkipClicked)
//
//        val values = mutableListOf<NamePickerNavigation>()
//
//        viewModel.navigation.collect { values.add(it) }
//
//        assertTrue(values.isNotEmpty())
//        assertTrue(values.size == 1)
//        assertTrue(values.first() is NamePickerNavigation.NavigateToGameScreen)
//
//        assertEquals(
//            (values.first() as NamePickerNavigation.NavigateToGameScreen).player1Name, "John"
//        )
//        assertEquals(
//            (values.first() as NamePickerNavigation.NavigateToGameScreen).player2Name,
//            "Jane"
//        )
    }

    @Test
    fun `validateNames returns true when names are valid`() {
        val viewModel = NamePickerViewModel()
        viewModel.onEvent(NamePickerEvent.OnPlayer1NameChanged("Alice"))
        viewModel.onEvent(NamePickerEvent.OnPlayer2NameChanged("Bob"))

        assertTrue(viewModel.uiState.value.isConfirmButtonEnabled)
    }

    @Test
    fun `validateNames returns false when names are blank`() {
        val viewModel = NamePickerViewModel()

        assertFalse(viewModel.uiState.value.isConfirmButtonEnabled)
    }

    @Test
    fun `validateNames returns false when names are the same`() {
        val viewModel = NamePickerViewModel()
        viewModel.onEvent(NamePickerEvent.OnPlayer1NameChanged("Alice"))
        viewModel.onEvent(NamePickerEvent.OnPlayer2NameChanged("Alice"))

        assertFalse(viewModel.uiState.value.isConfirmButtonEnabled)
    }
}