package com.example.tictactoe.domain.usecase

import com.example.tictactoe.domain.model.Board
import javax.inject.Inject

sealed interface MoveValidationResult {
    data object Valid : MoveValidationResult
    sealed interface Invalid : MoveValidationResult {
        data object CellOutOfBound : Invalid
        data object CellAlreadyOccupied : Invalid
    }
}

class MoveValidationUseCase @Inject constructor() {
    operator fun invoke(cellIndex: Int, board: Board): MoveValidationResult {
        return MoveValidationResult.Valid
    }
}
