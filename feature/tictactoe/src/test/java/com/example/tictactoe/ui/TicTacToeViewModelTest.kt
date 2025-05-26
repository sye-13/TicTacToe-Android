package com.example.tictactoe.ui

import app.cash.turbine.test
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.usecase.CheckGameStatusUseCase
import com.example.tictactoe.domain.usecase.GameStatusResult
import com.example.tictactoe.domain.usecase.MoveValidationResult
import com.example.tictactoe.domain.usecase.MoveValidationUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@ExperimentalCoroutinesApi
class TicTacToeViewModelTest {

    private lateinit var viewModel: TicTacToeViewModel
    private val mockMoveValidationUseCase: MoveValidationUseCase = mockk()
    private val mockCheckGameStatusUseCase: CheckGameStatusUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TicTacToeViewModel(mockMoveValidationUseCase, mockCheckGameStatusUseCase)
    }

    @Test
    fun `test valid move updates state correctly`() = runTest {
        every { mockMoveValidationUseCase(any(), any()) } returns MoveValidationResult.Valid
        every { mockCheckGameStatusUseCase(any()) } returns GameStatusResult.InProgress

        val cellIndex = 0
        viewModel.onCellClicked(cellIndex)
        advanceUntilIdle()

        viewModel.uiState.test {
            val firstEmission = awaitItem()
            assertTrue(firstEmission is TicTacToeState.Playing)

            val playingState = firstEmission as TicTacToeState.Playing
            assertEquals(playingState.currentPlayer, Player.O)  // Player X started
        }
    }

    @Test
    fun `test invalid move triggers validation error`() = runTest {
        val invalidMoveResult = MoveValidationResult.Invalid.CellOutOfBound
        every { mockMoveValidationUseCase(any(), any()) } returns invalidMoveResult

        viewModel.onCellClicked(-1)
        advanceUntilIdle()

        viewModel.uiState.test {
            val firstEmission = awaitItem()
            assertTrue(firstEmission is TicTacToeState.Playing)

            val playingState = firstEmission as TicTacToeState.Playing
            assertEquals(playingState.moveValidationResult, invalidMoveResult)
        }
    }

    @Test
    fun `test game over state when the game finishes`() = runTest {
        val gameOverResult = GameStatusResult.GameOver.Won(winner = Player.X)
        every { mockMoveValidationUseCase(any(), any()) } returns MoveValidationResult.Valid
        every { mockCheckGameStatusUseCase(any()) } returns gameOverResult

        val cellIndex = 0
        viewModel.onCellClicked(cellIndex)
        advanceUntilIdle()

        viewModel.uiState.test {
            val firstEmission = awaitItem()
            assertTrue(firstEmission is TicTacToeState.GameOver)

            val gameOverState = firstEmission as TicTacToeState.GameOver
            assertEquals(gameOverState.gameOutcome, gameOverResult)
        }
    }

    @Test
    fun `test new game reset`() = runTest {
        val gameOverResult = GameStatusResult.GameOver.Won(winner = Player.X)
        every { mockMoveValidationUseCase(any(), any()) } returns MoveValidationResult.Valid
        every { mockCheckGameStatusUseCase(any()) } returns gameOverResult
        viewModel.onCellClicked(0)
        advanceUntilIdle()

        viewModel.onNewGameClicked()
        advanceUntilIdle()

        viewModel.uiState.test {
            val playingEmission = awaitItem()
            assertTrue(playingEmission is TicTacToeState.Playing)
            val playingState = playingEmission as TicTacToeState.Playing
            assertEquals(playingState.currentPlayer, Player.X)  // Player X starts
        }
    }

    @Test
    fun `test no reset if game is not over`() = runTest {
        every { mockMoveValidationUseCase(any(), any()) } returns MoveValidationResult.Valid
        every { mockCheckGameStatusUseCase(any()) } returns GameStatusResult.InProgress
        viewModel.onCellClicked(0)
        advanceUntilIdle()

        viewModel.onNewGameClicked()
        advanceUntilIdle()

        viewModel.uiState.test {
            val firstEmission = awaitItem()
            assertTrue(firstEmission is TicTacToeState.Playing)
        }
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
