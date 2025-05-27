package com.example.tictactoe.ui

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.usecase.CheckGameStatusUseCase
import com.example.tictactoe.domain.usecase.GameStatusResult
import com.example.tictactoe.domain.usecase.MoveValidationResult
import com.example.tictactoe.domain.usecase.MoveValidationUseCase
import com.example.tictactoe.ui.model.BoardUi
import com.example.tictactoe.ui.model.GameStatusResultUi
import com.example.tictactoe.ui.model.MoveValidationResultUi
import com.example.tictactoe.ui.model.PlayerUi
import com.example.tictactoe.ui.model.applyMove
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

private const val savedStateKey = "savedState"

@OptIn(ExperimentalCoroutinesApi::class)
class TicTacToeViewModelTest {

    private lateinit var viewModel: TicTacToeViewModel
    private val moveValidationUseCase: MoveValidationUseCase = mockk()
    private val checkGameStatusUseCase: CheckGameStatusUseCase = mockk()
    private val savedStateHandle = SavedStateHandle()

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TicTacToeViewModel(
            savedStateHandle,
            moveValidationUseCase,
            checkGameStatusUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `default state is Playing`() = runTest {
        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is TicTacToeState.Playing)
        }
    }

    @Test
    fun `default playing state has empty board and X as current player`() = runTest {
        viewModel.uiState.test {
            val state = expectMostRecentItem()
            val playingState = state as TicTacToeState.Playing
            assertEquals(BoardUi.empty(), playingState.board)
            assertEquals(PlayerUi.X, playingState.currentPlayer)
        }
    }

    @Test
    fun `move on invalid cell shows validation error`() = runTest {
        every {
            moveValidationUseCase(
                any(),
                any()
            )
        } returns MoveValidationResult.Invalid.CellOutOfBound

        viewModel.onCellClicked(-1)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is TicTacToeState.Playing)
            assertEquals(
                MoveValidationResultUi.Invalid.CellOutOfBound,
                (state as TicTacToeState.Playing).moveValidationResult
            )
        }
    }

    @Test
    fun `valid move progresses to next player`() = runTest {
        every { moveValidationUseCase(any(), any()) } returns MoveValidationResult.Valid
        every { checkGameStatusUseCase(any()) } returns GameStatusResult.InProgress

        viewModel.onCellClicked(0)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is TicTacToeState.Playing)
            assertEquals(PlayerUi.O, (state as TicTacToeState.Playing).currentPlayer)
        }
    }

    @Test
    fun `valid move updates board`() = runTest {
        every { moveValidationUseCase(any(), any()) } returns MoveValidationResult.Valid
        every { checkGameStatusUseCase(any()) } returns GameStatusResult.InProgress

        viewModel.onCellClicked(0)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is TicTacToeState.Playing)
            assertEquals(
                BoardUi.empty().applyMove(0, PlayerUi.X),
                (state as TicTacToeState.Playing).board
            )
        }
    }

    @Test
    fun `move on already occupied cell shows validation error`() = runTest {
        savedStateHandle[savedStateKey] =
            TicTacToeState.Playing(
                board = BoardUi.empty().applyMove(0, PlayerUi.X),
                currentPlayer = PlayerUi.O
            )

        every {
            moveValidationUseCase(
                any(),
                any()
            )
        } returns MoveValidationResult.Invalid.CellAlreadyOccupied
        viewModel =
            TicTacToeViewModel(savedStateHandle, moveValidationUseCase, checkGameStatusUseCase)

        viewModel.onCellClicked(0)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is TicTacToeState.Playing)
            assertEquals(
                MoveValidationResultUi.Invalid.CellAlreadyOccupied,
                (state as TicTacToeState.Playing).moveValidationResult
            )
        }
    }

    @Test
    fun `move on already occupied cell does not toggle player`() = runTest {
        savedStateHandle[savedStateKey] =
            TicTacToeState.Playing(
                board = BoardUi.empty().applyMove(0, PlayerUi.X),
                currentPlayer = PlayerUi.O
            )
        every {
            moveValidationUseCase(
                any(),
                any()
            )
        } returns MoveValidationResult.Invalid.CellAlreadyOccupied
        viewModel =
            TicTacToeViewModel(savedStateHandle, moveValidationUseCase, checkGameStatusUseCase)

        viewModel.onCellClicked(0)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is TicTacToeState.Playing)
            assertEquals(PlayerUi.O, (state as TicTacToeState.Playing).currentPlayer)
        }
    }

    @Test
    fun `game ends when win condition met`() = runTest {
        every { moveValidationUseCase(any(), any()) } returns MoveValidationResult.Valid
        every { checkGameStatusUseCase(any()) } returns GameStatusResult.GameOver.Won(Player.X)

        viewModel.onCellClicked(0)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is TicTacToeState.GameOver)
            assertEquals(
                GameStatusResultUi.GameOver.Won(PlayerUi.X),
                (state as TicTacToeState.GameOver).gameOutcome
            )
        }
    }

    @Test
    fun `game ends in draw when board is full and no winner`() = runTest {
        every { moveValidationUseCase(any(), any()) } returns MoveValidationResult.Valid
        every { checkGameStatusUseCase(any()) } returns GameStatusResult.GameOver.Draw

        viewModel.onCellClicked(0)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is TicTacToeState.GameOver)
            assertEquals(
                GameStatusResultUi.GameOver.Draw,
                (state as TicTacToeState.GameOver).gameOutcome
            )
        }
    }

    @Test
    fun `new game resets the board after game over`() = runTest {
        savedStateHandle[savedStateKey] =
            TicTacToeState.GameOver(GameStatusResultUi.GameOver.Won(PlayerUi.X))
        viewModel =
            TicTacToeViewModel(savedStateHandle, moveValidationUseCase, checkGameStatusUseCase)

        viewModel.onNewGameClicked()

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is TicTacToeState.Playing)
            assertEquals(PlayerUi.X, (state as TicTacToeState.Playing).currentPlayer)
        }
    }

    @Test
    fun `new game does nothing if game is still in progress`() = runTest {
        every { moveValidationUseCase(any(), any()) } returns MoveValidationResult.Valid
        every { checkGameStatusUseCase(any()) } returns GameStatusResult.InProgress

        viewModel.onNewGameClicked()

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is TicTacToeState.Playing)
        }
    }

    @Test
    fun `cell click ignored after game is over`() = runTest {
        val gameOver = GameStatusResultUi.GameOver.Won(PlayerUi.X)
        savedStateHandle[savedStateKey] = TicTacToeState.GameOver(gameOver)
        viewModel =
            TicTacToeViewModel(savedStateHandle, moveValidationUseCase, checkGameStatusUseCase)

        viewModel.onCellClicked(0)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is TicTacToeState.GameOver)
            assertEquals(gameOver, (state as TicTacToeState.GameOver).gameOutcome)
        }
    }
}
