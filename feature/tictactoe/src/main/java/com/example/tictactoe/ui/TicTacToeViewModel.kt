package com.example.tictactoe.ui

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
import com.example.tictactoe.ui.model.toEntity
import com.example.tictactoe.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TicTacToeState {
    data class Playing(
        val board: BoardUi = BoardUi.empty(),
        val currentPlayer: Player = Player.X,
        val moveValidationResult: MoveValidationResult? = null,
    ) : TicTacToeState()

    data class GameOver(val gameOutcome: GameStatusResult.GameOver) : TicTacToeState()
}

@HiltViewModel
class TicTacToeViewModel @Inject constructor(
    private val moveValidationUseCase: MoveValidationUseCase,
    private val checkGameStatusUseCase: CheckGameStatusUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<TicTacToeState> =
        MutableStateFlow(TicTacToeState.Playing())

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
                    moveValidationResult = validationResult
                )
                return@launch
            }

            val updatedBoard = applyMove(currentBoard, cellIndex, state.currentPlayer)
            when (val gameStatus = checkGameStatusUseCase(updatedBoard)) {
                is GameStatusResult.GameOver -> {
                    _uiState.update {
                        TicTacToeState.GameOver(gameOutcome = gameStatus)
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
        currentPlayer: Player,
        moveValidationResult: MoveValidationResult? = null
    ) {
        _uiState.update {
            TicTacToeState.Playing(
                board = BoardUi(board.cells.map { it.toUiModel() }),
                currentPlayer = currentPlayer,
                moveValidationResult = moveValidationResult
            )
        }
    }

    private fun togglePlayer(currentPlayer: Player): Player =
        if (currentPlayer == Player.X) Player.O else Player.X

    private fun resetState() {
        _uiState.value = TicTacToeState.Playing()
    }
}
