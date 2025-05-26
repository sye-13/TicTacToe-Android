package com.example.tictactoe.ui.model

sealed interface MoveValidationResultUi {
    data object Valid : MoveValidationResultUi
    sealed interface Invalid : MoveValidationResultUi {
        data object CellOutOfBound : Invalid

        data object CellAlreadyOccupied : Invalid
    }
}
