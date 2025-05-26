package com.example.tictactoe.ui.model

sealed interface GameStatusResultUi {
    data object InProgress : GameStatusResultUi

    sealed interface GameOver : GameStatusResultUi {
        data object Draw : GameOver

        data class Won(val winner: PlayerUi) : GameOver
    }
}
