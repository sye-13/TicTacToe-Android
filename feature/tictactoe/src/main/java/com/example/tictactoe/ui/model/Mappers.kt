package com.example.tictactoe.ui.model

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.usecase.GameStatusResult
import com.example.tictactoe.domain.usecase.MoveValidationResult

fun Cell.toUiModel(): CellUi {
    return when (this) {
        is Cell.Empty -> CellUi.Empty
        is Cell.Occupied -> CellUi.Occupied(
            player = this.player.toUiModel(),
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
            player = this.player.toEntity(),
            isHighlighted = this.isHighlighted
        )
    }
}

fun BoardUi.toEntity(): Board {
    val cells = cells.map { cellUi -> cellUi.toEntity() }
    return Board(cells = cells)
}

fun MoveValidationResultUi.toEntity(): MoveValidationResult {
    return when (this) {
        MoveValidationResultUi.Invalid.CellAlreadyOccupied -> MoveValidationResult.Invalid.CellAlreadyOccupied
        MoveValidationResultUi.Invalid.CellOutOfBound -> MoveValidationResult.Invalid.CellOutOfBound
        MoveValidationResultUi.Valid -> MoveValidationResult.Valid
    }
}

fun MoveValidationResult.toUiModel(): MoveValidationResultUi {
    return when (this) {
        MoveValidationResult.Invalid.CellAlreadyOccupied -> MoveValidationResultUi.Invalid.CellAlreadyOccupied
        MoveValidationResult.Invalid.CellOutOfBound -> MoveValidationResultUi.Invalid.CellOutOfBound
        MoveValidationResult.Valid -> MoveValidationResultUi.Valid
    }
}

fun GameStatusResultUi.toEntity(): GameStatusResult {
    return when (this) {
        GameStatusResultUi.InProgress -> GameStatusResult.InProgress
        GameStatusResultUi.GameOver.Draw -> GameStatusResult.GameOver.Draw
        is GameStatusResultUi.GameOver.Won -> GameStatusResult.GameOver.Won(winner = winner.toEntity())
    }
}

fun GameStatusResult.toUiModel(): GameStatusResultUi {
    return when (this) {
        GameStatusResult.InProgress -> GameStatusResultUi.InProgress
        GameStatusResult.GameOver.Draw -> GameStatusResultUi.GameOver.Draw
        is GameStatusResult.GameOver.Won -> GameStatusResultUi.GameOver.Won(winner = winner.toUiModel())
    }
}

fun PlayerUi.toEntity(): Player {
    return when (this) {
        PlayerUi.X -> Player.X
        PlayerUi.O -> Player.O
    }
}

fun Player.toUiModel(): PlayerUi {
    return when (this) {
        Player.X -> PlayerUi.X
        Player.O -> PlayerUi.O
    }
}
