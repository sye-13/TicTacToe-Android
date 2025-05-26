package com.example.tictactoe.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.designsystem.ui.theme.TicTacToeTheme

@Composable
fun TicTacToeScreen() {
    Text("Tic Tac Toe")
}

@Preview(showBackground = true)
@Composable
fun TicTacToeScreenPreview() {
    TicTacToeTheme {
        TicTacToeScreen()
    }
}
