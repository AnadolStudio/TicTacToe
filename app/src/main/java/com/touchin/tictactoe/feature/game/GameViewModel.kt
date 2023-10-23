package com.touchin.tictactoe.feature.game

import android.graphics.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.touchin.tictactoe.base.viewmodel.BaseContentViewModel
import com.touchin.tictactoe.base.viewmodel.navigateUp

class GameViewModel(
    gameType: GameType
) : BaseContentViewModel<GameState>(
    GameState(gameType)
), GameController {

    override fun onItemClicked(row: Int, column: Int) {
        val allPlayers = state.players.toMutableList()

        val currentPlayerPoints = state.currentPlayer.points.toMutableList().apply {
            add(GamePoint(row = row, column = column))
        }
        val currentPlayer = state.currentPlayer.copy(points = currentPlayerPoints)

        val currentPlayerIndex = allPlayers.indexOfFirst { it.name == currentPlayer.name }
        val nextPlayerIndex = (currentPlayerIndex + 1) % allPlayers.size
        val nextPlayer = allPlayers[nextPlayerIndex]

        allPlayers.removeAt(currentPlayerIndex)
        allPlayers.add(currentPlayerIndex, currentPlayer)

        updateState {
            val winner = checkWinner(currentPlayer)

            copy(
                currentPlayer = nextPlayer,
                players = allPlayers,
                winnerPlayer = winner,
                isEnd = winner != null
            )
        }
    }

    private fun checkWinner(currentPlayer: Player): Player? {
        val winnerPoints = findWinnerPoints(currentPlayer) ?: return null

        showEvent(
            GameEvents.Win(
                player = currentPlayer,
                startColumn = winnerPoints.left,
                startRow = winnerPoints.top,
                endColumn = winnerPoints.right,
                endRow = winnerPoints.bottom,
            )
        )

        return currentPlayer
    }

    private fun findWinnerPoints(currentPlayer: Player): Rect? {
        return null
    }

    private fun checkRightDiagonal(points: List<GamePoint>, gamePoint: GamePoint): Rect? =
        getRectOrNull(
            startPoint = points.firstOrNull { it.row == gamePoint.row - 1 && it.column + 1 == gamePoint.column },
            endPoint = points.firstOrNull { it.row == gamePoint.row + 1 && it.column - 1 == gamePoint.column }
        )

    private fun checkLeftDiagonal(points: List<GamePoint>, gamePoint: GamePoint): Rect? =
        getRectOrNull(
            startPoint = points.firstOrNull { it.row == gamePoint.row - 1 && it.column - 1 == gamePoint.column },
            endPoint = points.firstOrNull { it.row == gamePoint.row + 1 && it.column + 1 == gamePoint.column }
        )

    private fun getRectOrNull(
        startPoint: GamePoint?,
        endPoint: GamePoint?
    ): Rect? = when (startPoint == null || endPoint == null) {
        true -> null
        else -> Rect(startPoint.column, startPoint.row, endPoint.column, endPoint.row)
    }

    override fun restart() {
        showEvent(GameEvents.Restart)
        updateState { initState }
    }

    override fun onBackClicked() = _navigationEvent.navigateUp()

    @Suppress("UNCHECKED_CAST")
    class Factory(private val gameType: GameType) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GameViewModel(gameType) as T
        }
    }

}
