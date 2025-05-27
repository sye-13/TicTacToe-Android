package com.example.tictactoe.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BoardUi(val cells: List<CellUi>) : Parcelable {
    companion object {
        fun empty() = BoardUi(List(9) { CellUi.Empty })
    }
}
