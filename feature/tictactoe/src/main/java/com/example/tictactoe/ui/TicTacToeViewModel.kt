package com.example.tictactoe.ui

import androidx.lifecycle.ViewModel
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.usecase.CheckGameStatusUseCase
import com.example.tictactoe.domain.usecase.GameStatusResult
import com.example.tictactoe.domain.usecase.MoveValidationResult
import com.example.tictactoe.domain.usecase.MoveValidationUseCase
import com.example.tictactoe.ui.model.BoardUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun onCellClicked(cellIndex: Int) {}

    fun onNewGameClicked() {}
}
