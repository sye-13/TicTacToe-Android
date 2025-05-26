package com.example.tictactoe.ui.model

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.usecase.GameStatusResult
import com.example.tictactoe.domain.usecase.MoveValidationResult

fun BoardUi.toEntity(): Board {
    val cells = cells.map { cellUi -> cellUi.toEntity() }
    return Board(cells = cells)
}

fun MoveValidationResult.toUiModel(): MoveValidationResultUi {
    return when (this) {
        MoveValidationResult.Invalid.CellAlreadyOccupied -> MoveValidationResultUi.Invalid.CellAlreadyOccupied
        MoveValidationResult.Invalid.CellOutOfBound -> MoveValidationResultUi.Invalid.CellOutOfBound
        MoveValidationResult.Valid -> MoveValidationResultUi.Valid
    }
}

fun GameStatusResult.toUiModel(): GameStatusResultUi {
    return when (this) {
        GameStatusResult.InProgress -> GameStatusResultUi.InProgress
        GameStatusResult.GameOver.Draw -> GameStatusResultUi.GameOver.Draw
        is GameStatusResult.GameOver.Won -> GameStatusResultUi.GameOver.Won(winner = winner.toUiModel())
    }
}

fun Player.toUiModel(): PlayerUi {
    return when (this) {
        Player.X -> PlayerUi.X
        Player.O -> PlayerUi.O
    }
}

fun BoardUi.applyMove(cellIndex: Int, player: PlayerUi): BoardUi {
    val newCells = cells.toMutableList()
    newCells[cellIndex] = CellUi.Occupied(player)
    return BoardUi(newCells)
}

private fun CellUi.toEntity(): Cell {
    return when (this) {
        is CellUi.Empty -> Cell.Empty
        is CellUi.Occupied -> Cell.Occupied(
            player = player.toEntity(),
            isHighlighted = isHighlighted
        )
    }
}

private fun PlayerUi.toEntity(): Player {
    return when (this) {
        PlayerUi.X -> Player.X
        PlayerUi.O -> Player.O
    }
}
