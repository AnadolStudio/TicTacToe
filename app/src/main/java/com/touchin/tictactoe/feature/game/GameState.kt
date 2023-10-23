package com.touchin.tictactoe.feature.game

import android.graphics.Color
import com.touchin.tictactoe.R

data class GameState(
    val type: GameType,
    val players: List<Player> = createPlayers(type),
    val currentPlayer: Player = players.first(),
    val winnerPlayer: Player? = null,
    val isEnd: Boolean = false
)

fun createPlayers(type: GameType): List<Player> {
    return when (type) {
        GameType.DUO -> listOf(
            Player(name = "Орда", shapeDrawableRes = R.drawable.ic_circle, color = Color.RED),
            Player(name = "Альянс", shapeDrawableRes = R.drawable.ic_cross, color = Color.BLUE)
        )

        GameType.TRIO -> listOf(
            Player(name = "Орда", shapeDrawableRes = R.drawable.ic_circle, color = Color.RED),
            Player(name = "Альянс", shapeDrawableRes = R.drawable.ic_cross, color = Color.BLUE),
            Player(name = "Империя", shapeDrawableRes = R.drawable.ic_triangle, color = Color.MAGENTA),
        )
    }
}
