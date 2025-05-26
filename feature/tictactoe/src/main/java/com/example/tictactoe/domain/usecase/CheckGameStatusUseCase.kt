package com.example.tictactoe.domain.usecase

import com.example.tictactoe.domain.model.Board
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
        return GameStatusResult.InProgress
    }
}
