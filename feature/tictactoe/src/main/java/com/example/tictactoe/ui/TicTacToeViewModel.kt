package com.example.tictactoe.ui

import androidx.lifecycle.ViewModel
import com.example.tictactoe.domain.usecase.CheckGameStatusUseCase
import com.example.tictactoe.domain.usecase.MoveValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicTacToeViewModel @Inject constructor(
    private val moveValidationUseCase: MoveValidationUseCase,
    private val checkGameStatusUseCase: CheckGameStatusUseCase,
) : ViewModel() {
}
