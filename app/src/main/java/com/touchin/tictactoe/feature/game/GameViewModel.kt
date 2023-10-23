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
            val hasEmptyPoint = allPlayers.flatMap { it.points }.size == state.type.size()

            copy(
                currentPlayer = nextPlayer,
                players = allPlayers,
                winnerPlayer = winner,
                isEnd = winner != null || hasEmptyPoint
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
        for (gamePoint in currentPlayer.points) {

            val winPoints = listOfNotNull(
                checkSupportDiagonal(currentPlayer.points, gamePoint),
                checkMainDiagonal(currentPlayer.points, gamePoint),
                checkHorizontal(currentPlayer.points, gamePoint),
                checkVertical(currentPlayer.points, gamePoint),
            )

            if (winPoints.isNotEmpty()) return winPoints.first()
        }

        return null
    }

    private fun checkVertical(pointList: List<GamePoint>, point: GamePoint): Rect? = getRectOrNull(
        startPoint = pointList.getLastPoint { iteration, currentPoint ->
            currentPoint.row == point.row - iteration && currentPoint.column == point.column
        },
        endPoint = pointList.getLastPoint { iteration, currentPoint ->
            currentPoint.row == point.row + iteration && currentPoint.column == point.column
        }
    )

    private fun checkHorizontal(pointList: List<GamePoint>, point: GamePoint): Rect? = getRectOrNull(
        startPoint = pointList.getLastPoint { iteration, currentPoint ->
            currentPoint.row == point.row && currentPoint.column - iteration == point.column
        },
        endPoint = pointList.getLastPoint { iteration, currentPoint ->
            currentPoint.row == point.row && currentPoint.column + iteration == point.column
        }
    )

    private fun checkSupportDiagonal(pointList: List<GamePoint>, point: GamePoint): Rect? = getRectOrNull(
        startPoint = pointList.getLastPoint { iteration, currentPoint ->
            currentPoint.row == point.row - iteration && currentPoint.column + iteration == point.column
        },
        endPoint = pointList.getLastPoint { iteration, currentPoint ->
            currentPoint.row == point.row + iteration && currentPoint.column - iteration == point.column
        }
    )

    private fun checkMainDiagonal(pointList: List<GamePoint>, point: GamePoint): Rect? = getRectOrNull(
        startPoint = pointList.getLastPoint { iteration, currentPoint ->
            currentPoint.row == point.row - iteration && currentPoint.column - iteration == point.column
        },
        endPoint = pointList.getLastPoint { iteration, currentPoint ->
            currentPoint.row == point.row + iteration && currentPoint.column + iteration == point.column
        }
    )

    private fun List<GamePoint>.getLastPoint(action: (iteration: Int, currentPoint: GamePoint) -> Boolean): GamePoint? {
        var resultPoint: GamePoint? = null
        var iteration = 1

        do {
            val currentPoint = firstOrNull { action.invoke(iteration, it) }

            if (currentPoint == null) {
                break
            } else {
                resultPoint = currentPoint
                iteration++
            }

        } while (resultPoint != null)

        return resultPoint
    }

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
