package com.example.tictactoe.domain.usecase

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.Player
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckGameStatusUseCaseTest {

    private val useCase = CheckGameStatusUseCase()

    @Test
    fun `returns Won when a player has a winning row`() {
        val board = Board(
            listOf(
                Cell.Occupied(Player.X), Cell.Occupied(Player.X), Cell.Occupied(Player.X),
                Cell.Empty, Cell.Empty, Cell.Empty,
                Cell.Empty, Cell.Empty, Cell.Empty
            )
        )

        val result = useCase(board)

        assertEquals(GameStatusResult.GameOver.Won(Player.X), result)
    }

    @Test
    fun `returns Draw when all cells are occupied and no winner`() {
        val board = Board(
            listOf(
                Cell.Occupied(Player.X), Cell.Occupied(Player.O), Cell.Occupied(Player.X),
                Cell.Occupied(Player.X), Cell.Occupied(Player.O), Cell.Occupied(Player.O),
                Cell.Occupied(Player.O), Cell.Occupied(Player.X), Cell.Occupied(Player.X)
            )
        )

        val result = useCase(board)

        assertEquals(GameStatusResult.GameOver.Draw, result)
    }

    @Test
    fun `returns InProgress when there are empty cells and no winner`() {
        val board = Board(
            listOf(
                Cell.Occupied(Player.X), Cell.Occupied(Player.O), Cell.Empty,
                Cell.Empty, Cell.Occupied(Player.X), Cell.Empty,
                Cell.Empty, Cell.Empty, Cell.Occupied(Player.O)
            )
        )

        val result = useCase(board)

        assertEquals(GameStatusResult.InProgress, result)
    }
}