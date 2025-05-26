package com.example.tictactoe.domain.usecase

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.Player
import org.junit.Assert.assertEquals
import org.junit.Test

class MoveValidationUseCaseTest {

    private val useCase = MoveValidationUseCase()

    @Test
    fun `returns Valid when cell index is within bounds and cell is empty`() {
        val board = Board(cells = List(9) { Cell.Empty })
        val result = useCase.invoke(4, board)
        assertEquals(MoveValidationResult.Valid, result)
    }

    @Test
    fun `returns CellOutOfBound when cell index is negative`() {
        val board = Board(cells = List(9) { Cell.Empty })
        val result = useCase.invoke(-1, board)
        assertEquals(MoveValidationResult.Invalid.CellOutOfBound, result)
    }

    @Test
    fun `returns CellOutOfBound when cell index exceeds upper bound`() {
        val board = Board(cells = List(9) { Cell.Empty })
        val result = useCase.invoke(9, board)
        assertEquals(MoveValidationResult.Invalid.CellOutOfBound, result)
    }

    @Test
    fun `returns CellAlreadyOccupied when cell is already occupied with X`() {
        val cells = List(9) { index ->
            if (index == 4) Cell.Occupied(Player.X) else Cell.Empty
        }
        val board = Board(cells = cells)
        val result = useCase.invoke(4, board)
        assertEquals(MoveValidationResult.Invalid.CellAlreadyOccupied, result)
    }

    @Test
    fun `returns CellAlreadyOccupied when cell is already occupied with O`() {
        val cells = List(9) { index ->
            if (index == 4) Cell.Occupied(Player.O) else Cell.Empty
        }
        val board = Board(cells = cells)
        val result = useCase.invoke(4, board)
        assertEquals(MoveValidationResult.Invalid.CellAlreadyOccupied, result)
    }
}
