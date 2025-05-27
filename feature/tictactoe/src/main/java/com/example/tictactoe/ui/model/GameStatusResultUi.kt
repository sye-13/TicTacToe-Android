package com.example.tictactoe.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface GameStatusResultUi : Parcelable {

    @Parcelize
    data object InProgress : GameStatusResultUi

    @Parcelize
    sealed interface GameOver : GameStatusResultUi {

        @Parcelize
        data object Draw : GameOver

        @Parcelize
        data class Won(val winner: PlayerUi) : GameOver
    }
}
