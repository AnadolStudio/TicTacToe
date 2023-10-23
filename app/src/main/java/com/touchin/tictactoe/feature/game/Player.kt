package com.touchin.tictactoe.feature.game

import androidx.annotation.DrawableRes

data class Player(
    val name: String,
    @DrawableRes val shapeDrawableRes: Int,
    val color: Int,
    val points: List<GamePoint> = emptyList()
)

data class GamePoint(val row: Int, val column: Int) {

    fun toIndex(width: Int): Int = row * width + column
}
