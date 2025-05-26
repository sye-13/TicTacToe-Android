package com.example.tictactoe.ui

import androidx.lifecycle.SavedStateHandle
import com.example.tictactoe.ui.model.BoardUi
import com.example.tictactoe.ui.model.CellUi
import com.example.tictactoe.ui.model.GameStatusResultUi
import com.example.tictactoe.ui.model.PlayerUi

private const val boardKey = "board"
private const val currentPlayerKey = "currentPlayer"
private const val gameOverKey = "gameOver"
private const val gameOutcomeIsDrawKey = "gameOutcomeIsDraw"
private const val gameOutcomeWinnerKey = "gameOutcomeWinner"

fun SavedStateHandle.saveState(state: TicTacToeState) {
    when (state) {
        is TicTacToeState.Playing -> {
            saveGameOver(isGameOver = false)
            saveBoard(state.board)
            saveCurrentPlayer(state.currentPlayer)
        }

        is TicTacToeState.GameOver -> {
            saveGameOver(isGameOver = true)
            when (val outcome = state.gameOutcome) {
                GameStatusResultUi.GameOver.Draw -> saveDraw()
                is GameStatusResultUi.GameOver.Won -> saveWinner(outcome.winner)
            }
        }
    }
}

fun SavedStateHandle.restoreState(): TicTacToeState {
    if (!isGameOver()) {
        return TicTacToeState.Playing(
            board = BoardUi(getBoard()),
            currentPlayer = getCurrentPlayer() ?: PlayerUi.X,
        )
    }
    if (isDraw()) {
        return TicTacToeState.GameOver(gameOutcome = GameStatusResultUi.GameOver.Draw)
    }
    val winner = getWinner()
        ?: return TicTacToeState.GameOver(gameOutcome = GameStatusResultUi.GameOver.Draw)
    return TicTacToeState.GameOver(
        gameOutcome = GameStatusResultUi.GameOver.Won(winner = winner)
    )
}

private fun SavedStateHandle.saveBoard(board: BoardUi) {
    this[boardKey] = board.cells.map { it.toRawInt() }
}

private fun SavedStateHandle.getBoard(): List<CellUi> {
    return this.get<List<Int>>(boardKey)?.map { it.toCellUi() } ?: List(9) { CellUi.Empty }
}

private fun SavedStateHandle.saveCurrentPlayer(player: PlayerUi) {
    this[currentPlayerKey] = player.name
}

private fun SavedStateHandle.getCurrentPlayer(): PlayerUi? {
    return this.get<String>(currentPlayerKey)?.let { PlayerUi.valueOf(it) }
}

private fun SavedStateHandle.saveGameOver(isGameOver: Boolean) {
    this[gameOverKey] = isGameOver
}

private fun SavedStateHandle.isGameOver(): Boolean {
    return this.get<Boolean>(gameOverKey) ?: false
}

private fun SavedStateHandle.saveDraw() {
    this[gameOutcomeIsDrawKey] = true
    this.remove<String>(gameOutcomeWinnerKey)
}

private fun SavedStateHandle.isDraw(): Boolean {
    return this.get<Boolean>(gameOutcomeIsDrawKey) ?: false
}

private fun SavedStateHandle.saveWinner(player: PlayerUi) {
    this[gameOutcomeWinnerKey] = player.name
}

private fun SavedStateHandle.getWinner(): PlayerUi? {
    return this.get<String>(gameOutcomeWinnerKey)?.let { PlayerUi.valueOf(it) }
}

private fun CellUi.toRawInt(): Int = when (this) {
    is CellUi.Occupied -> if (player == PlayerUi.X) 1 else 2
    is CellUi.Empty -> 0
}

private fun Int.toCellUi(): CellUi = when (this) {
    1 -> CellUi.Occupied(PlayerUi.X)
    2 -> CellUi.Occupied(PlayerUi.O)
    else -> CellUi.Empty
}
