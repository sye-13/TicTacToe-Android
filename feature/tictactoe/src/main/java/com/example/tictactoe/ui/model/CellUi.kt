package com.example.tictactoe.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface CellUi : Parcelable {
    @Parcelize
    data object Empty : CellUi

    @Parcelize
    data class Occupied(val player: PlayerUi, val isHighlighted: Boolean = false) : CellUi
}
