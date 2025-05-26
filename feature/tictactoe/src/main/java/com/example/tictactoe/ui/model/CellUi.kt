package com.example.tictactoe.ui.model

import com.example.tictactoe.domain.model.Player

sealed interface CellUi {
    data object Empty : CellUi
    data class Occupied(val player: Player, val isHighlighted: Boolean = false) : CellUi
}
