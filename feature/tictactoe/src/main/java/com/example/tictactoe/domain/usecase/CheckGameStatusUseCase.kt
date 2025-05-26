package com.example.tictactoe.domain.usecase

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.Player
import javax.inject.Inject

sealed interface GameStatusResult {
    data object InProgress : GameStatusResult
    sealed interface GameOver : GameStatusResult {
        data object Draw : GameOver
        data class Won(val winner: Player) : GameOver
    }
}

class CheckGameStatusUseCase @Inject constructor() {
    operator fun invoke(board: Board): GameStatusResult {
        val cells = board.cells

        val winningLines = listOf(
            listOf(0, 1, 2), // Rows
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6), // Columns
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8), // Diagonals
            listOf(2, 4, 6)
        )

        for (line in winningLines) {
            val (a, b, c) = line
            val cellA = cells[a]
            val cellB = cells[b]
            val cellC = cells[c]

            if (cellA is Cell.Occupied &&
                cellB is Cell.Occupied &&
                cellC is Cell.Occupied &&
                cellA.player == cellB.player &&
                cellA.player == cellC.player
            ) {
                return GameStatusResult.GameOver.Won(cellA.player)
            }
        }

        return if (cells.any { it is Cell.Empty }) {
            GameStatusResult.InProgress
        } else {
            GameStatusResult.GameOver.Draw
        }
    }
}
