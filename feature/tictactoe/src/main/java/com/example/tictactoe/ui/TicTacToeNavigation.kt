package com.example.tictactoe.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object TicTacToe

fun NavGraphBuilder.ticTacToeScreen() {
    composable<TicTacToe> {
        TicTacToeRoute()
    }
}

@Composable
fun TicTacToeRoute() {
    TicTacToeScreen()
}
