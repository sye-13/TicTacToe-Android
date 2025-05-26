package com.example.tictactoe.ui.model

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell

fun Cell.toUiModel(): CellUi {
    return when (this) {
        is Cell.Empty -> CellUi.Empty
        is Cell.Occupied -> CellUi.Occupied(
            player = this.player,
            isHighlighted = this.isHighlighted
        )
    }
}

fun Board.toUiModel(): BoardUi {
    val cellUis = cells.map { cell -> cell.toUiModel() }
    return BoardUi(cells = cellUis)
}

fun CellUi.toEntity(): Cell {
    return when (this) {
        is CellUi.Empty -> Cell.Empty
        is CellUi.Occupied -> Cell.Occupied(
            player = this.player,
            isHighlighted = this.isHighlighted
        )
    }
}

fun BoardUi.toEntity(): Board {
    val cells = cells.map { cellUi -> cellUi.toEntity() }
    return Board(cells = cells)
}
