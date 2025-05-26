package com.example.tictactoe.domain.usecase

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
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
        if (cellIndex !in board.cells.indices) {
            return MoveValidationResult.Invalid.CellOutOfBound
        }
        if (board.cells[cellIndex] is Cell.Occupied) {
            return MoveValidationResult.Invalid.CellAlreadyOccupied
        }
        return MoveValidationResult.Valid
    }
}
