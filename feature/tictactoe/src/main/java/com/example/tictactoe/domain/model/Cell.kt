package com.example.tictactoe.domain.model

sealed interface Cell {
    data object Empty : Cell
    data class Occupied(val player: Player, val isHighlighted: Boolean = false) : Cell
}
