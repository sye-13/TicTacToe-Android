package com.example.tictactoe.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface MoveValidationResultUi : Parcelable {
    @Parcelize
    data object Valid : MoveValidationResultUi
    sealed interface Invalid : MoveValidationResultUi {
        @Parcelize
        data object CellOutOfBound : Invalid

        @Parcelize
        data object CellAlreadyOccupied : Invalid
    }
}
