package com.example.tictactoe.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.domain.usecase.CheckGameStatusUseCase
import com.example.tictactoe.domain.usecase.GameStatusResult
import com.example.tictactoe.domain.usecase.MoveValidationResult
import com.example.tictactoe.domain.usecase.MoveValidationUseCase
import com.example.tictactoe.ui.model.BoardUi
import com.example.tictactoe.ui.model.GameStatusResultUi
import com.example.tictactoe.ui.model.MoveValidationResultUi
import com.example.tictactoe.ui.model.PlayerUi
import com.example.tictactoe.ui.model.applyMove
import com.example.tictactoe.ui.model.toEntity
import com.example.tictactoe.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TicTacToeState {
    data class Playing(
        val board: BoardUi = BoardUi.empty(),
        val currentPlayer: PlayerUi = PlayerUi.X,
        val moveValidationResult: MoveValidationResultUi? = null,
    ) : TicTacToeState

    data class GameOver(val gameOutcome: GameStatusResultUi.GameOver) : TicTacToeState
}

@HiltViewModel
class TicTacToeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val moveValidationUseCase: MoveValidationUseCase,
    private val checkGameStatusUseCase: CheckGameStatusUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<TicTacToeState> =
        MutableStateFlow(savedStateHandle.restoreState())

    val uiState: StateFlow<TicTacToeState> = _uiState.asStateFlow()

    fun onCellClicked(cellIndex: Int) {
        val state = _uiState.value
        if (state !is TicTacToeState.Playing) return

        viewModelScope.launch {
            val validationResult = moveValidationUseCase(cellIndex, state.board.toEntity())

            if (validationResult is MoveValidationResult.Invalid) {
                updateState(state.copy(moveValidationResult = validationResult.toUiModel()))
                return@launch
            }

            val updatedBoard = state.board.applyMove(cellIndex, state.currentPlayer)
            when (val gameStatus = checkGameStatusUseCase(updatedBoard.toEntity())) {
                is GameStatusResult.GameOver -> updateState(
                    TicTacToeState.GameOver(gameOutcome = gameStatus.toUiModel() as GameStatusResultUi.GameOver)
                )

                is GameStatusResult.InProgress -> updateState(
                    TicTacToeState.Playing(
                        board = updatedBoard,
                        currentPlayer = togglePlayer(state.currentPlayer)
                    )
                )
            }
        }
    }

    fun onNewGameClicked() {
        val state = _uiState.value
        if (state !is TicTacToeState.GameOver) return

        updateState(TicTacToeState.Playing())
    }

    private fun togglePlayer(currentPlayer: PlayerUi): PlayerUi =
        if (currentPlayer == PlayerUi.X) PlayerUi.O else PlayerUi.X

    private fun updateState(newState: TicTacToeState) {
        _uiState.update { newState }
        savedStateHandle.saveState(_uiState.value)
    }
}
