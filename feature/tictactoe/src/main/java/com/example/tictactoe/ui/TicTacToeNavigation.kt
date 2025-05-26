package com.example.tictactoe.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
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
    val viewModel = hiltViewModel<TicTacToeViewModel>()
    TicTacToeScreen()
}
