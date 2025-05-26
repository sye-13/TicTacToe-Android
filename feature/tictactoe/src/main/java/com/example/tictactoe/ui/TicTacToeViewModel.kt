package com.example.tictactoe.ui

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.usecase.CheckGameStatusUseCase
import com.example.tictactoe.domain.usecase.GameStatusResult
import com.example.tictactoe.domain.usecase.MoveValidationResult
import com.example.tictactoe.domain.usecase.MoveValidationUseCase
import com.example.tictactoe.ui.model.BoardUi
import com.example.tictactoe.ui.model.GameStatusResultUi
import com.example.tictactoe.ui.model.MoveValidationResultUi
import com.example.tictactoe.ui.model.PlayerUi
import com.example.tictactoe.ui.model.toEntity
import com.example.tictactoe.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
sealed class TicTacToeState : Parcelable {
    @Parcelize
    data class Playing(
        val board: BoardUi = BoardUi.empty(),
        val currentPlayer: PlayerUi = PlayerUi.X,
        val moveValidationResult: MoveValidationResultUi? = null,
    ) : TicTacToeState()

    @Parcelize
    data class GameOver(val gameOutcome: GameStatusResultUi.GameOver) : TicTacToeState()
}

private const val uiStateKey = "uiState"

@HiltViewModel
class TicTacToeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val moveValidationUseCase: MoveValidationUseCase,
    private val checkGameStatusUseCase: CheckGameStatusUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<TicTacToeState> =
        MutableStateFlow(
            savedStateHandle.get<TicTacToeState>(uiStateKey) ?: TicTacToeState.Playing()
        )

    val uiState: StateFlow<TicTacToeState> = _uiState.asStateFlow()

    fun onCellClicked(cellIndex: Int) {
        val state = _uiState.value
        if (state !is TicTacToeState.Playing) return

        viewModelScope.launch {
            val currentBoard = state.toBoardEntity()
            val validationResult = moveValidationUseCase(cellIndex, currentBoard)

            if (validationResult is MoveValidationResult.Invalid) {
                emitPlayingState(
                    board = currentBoard,
                    currentPlayer = state.currentPlayer,
                    moveValidationResult = validationResult.toUiModel()
                )
                return@launch
            }

            val updatedBoard = applyMove(currentBoard, cellIndex, state.currentPlayer.toEntity())
            when (val gameStatus = checkGameStatusUseCase(updatedBoard)) {
                is GameStatusResult.GameOver -> {
                    _uiState.update {
                        TicTacToeState.GameOver(gameOutcome = gameStatus.toUiModel() as GameStatusResultUi.GameOver)
                    }
                }

                is GameStatusResult.InProgress -> {
                    emitPlayingState(
                        board = updatedBoard, currentPlayer = togglePlayer(state.currentPlayer)
                    )
                }
            }
        }
    }

    fun onNewGameClicked() {
        val state = _uiState.value
        if (state !is TicTacToeState.GameOver) return
        resetState()
    }

    private fun TicTacToeState.Playing.toBoardEntity(): Board {
        return Board(this.board.cells.map { it.toEntity() })
    }

    private fun applyMove(board: Board, cellIndex: Int, player: Player): Board {
        val newCells = board.cells.toMutableList()
        newCells[cellIndex] = Cell.Occupied(player)
        return Board(newCells)
    }

    private fun emitPlayingState(
        board: Board,
        currentPlayer: PlayerUi,
        moveValidationResult: MoveValidationResultUi? = null
    ) {
        _uiState.update {
            TicTacToeState.Playing(
                board = BoardUi(board.cells.map { it.toUiModel() }),
                currentPlayer = currentPlayer,
                moveValidationResult = moveValidationResult
            )
        }
        saveState(_uiState.value)
    }

    private fun togglePlayer(currentPlayer: PlayerUi): PlayerUi =
        if (currentPlayer == PlayerUi.X) PlayerUi.O else PlayerUi.X

    private fun resetState() {
        _uiState.update {
            TicTacToeState.Playing()
        }
        saveState(_uiState.value)
    }

    private fun saveState(state: TicTacToeState) {
        savedStateHandle[uiStateKey] = state
    }
}
