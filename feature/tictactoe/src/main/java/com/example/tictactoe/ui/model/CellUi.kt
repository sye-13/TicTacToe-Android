package com.example.tictactoe.ui.model

sealed interface CellUi {
    data object Empty : CellUi

    data class Occupied(val player: PlayerUi, val isHighlighted: Boolean = false) : CellUi
}
