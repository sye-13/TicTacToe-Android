package com.example.tictactoe.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.ui.theme.TicTacToeTheme
import com.example.tictactoe.R
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.usecase.GameStatusResult
import com.example.tictactoe.domain.usecase.MoveValidationResult
import com.example.tictactoe.ui.model.BoardUi
import com.example.tictactoe.ui.model.CellUi

@Composable
fun TicTacToeScreen(
    uiState: TicTacToeState,
    onCellClicked: (Int) -> Unit,
    onNewGameClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is TicTacToeState.Playing -> Playing(
                board = uiState.board,
                currentPlayer = uiState.currentPlayer,
                onCellClicked = onCellClicked,
                moveValidationResult = uiState.moveValidationResult
            )

            is TicTacToeState.GameOver -> GameOver(
                gameOutcome = uiState.gameOutcome,
                onNewGameClicked = onNewGameClicked
            )
        }
    }
}

@Composable
private fun Playing(
    board: BoardUi,
    currentPlayer: Player,
    onCellClicked: (Int) -> Unit,
    moveValidationResult: MoveValidationResult?
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.current_player, currentPlayer),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        BoardGrid(
            board = board,
            onCellClicked = onCellClicked
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.height(48.dp)) {
            moveValidationResult?.let {
                Text(
                    text = when (it) {
                        is MoveValidationResult.Invalid.CellOutOfBound -> stringResource(R.string.invalid_move_out_of_bound)
                        is MoveValidationResult.Invalid.CellAlreadyOccupied -> stringResource(R.string.invalid_move_already_occupied)
                        MoveValidationResult.Valid -> ""
                    },
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BoardGrid(board: BoardUi, onCellClicked: (Int) -> Unit) {
    Column {
        for (row in 0 until 3) {
            Row {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    val cell = board.cells[index]
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(2.dp, Color.Black)
                    ) {
                        Button(
                            onClick = { onCellClicked(index) },
                            modifier = Modifier.fillMaxSize(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            )
                        ) {
                            Cell(cell)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Cell(cell: CellUi) {
    Text(
        text = when (cell) {
            is CellUi.Occupied -> cell.player.name
            is CellUi.Empty -> ""
        },
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun GameOver(
    gameOutcome: GameStatusResult.GameOver,
    onNewGameClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.game_over),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (gameOutcome) {
            is GameStatusResult.GameOver.Draw -> Text(
                text = stringResource(R.string.outcome_draw),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            is GameStatusResult.GameOver.Won ->
                Text(
                    text = stringResource(R.string.outcome_winner, gameOutcome.winner.name),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNewGameClicked() }) {
            Text(text = stringResource(R.string.new_game))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicTacToeScreenPreview() {
    TicTacToeTheme {
        TicTacToeScreen(
            uiState = TicTacToeState.Playing(),
            onCellClicked = {},
            onNewGameClicked = {}
        )
    }
}
