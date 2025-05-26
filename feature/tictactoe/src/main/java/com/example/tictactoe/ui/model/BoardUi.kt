package com.example.tictactoe.ui.model

data class BoardUi(val cells: List<CellUi>) {
    companion object {
        fun empty() = BoardUi(List(9) { CellUi.Empty })
    }
}
