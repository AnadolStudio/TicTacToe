package com.touchin.tictactoe.feature.game

enum class GameType(val gridSide: Int) {
    DUO(3),
    TRIO(5);

    fun size(): Int = gridSide * gridSide
}
