package com.example.tictactoe

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.ui.TicTacToe
import com.example.tictactoe.ui.ticTacToeScreen

@Composable
fun App() {
    Surface(
        modifier = Modifier.safeDrawingPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = TicTacToe) {
            ticTacToeScreen()
        }
    }
}
