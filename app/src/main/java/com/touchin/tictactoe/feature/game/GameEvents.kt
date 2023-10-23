package com.touchin.tictactoe.feature.game

import com.anadolstudio.core.viewmodel.livedata.SingleCustomEvent

sealed class GameEvents : SingleCustomEvent() {

    object Restart : GameEvents()

    class Win(
        val player: Player,
        val startColumn: Int,
        val startRow: Int,
        val endColumn: Int,
        val endRow: Int,
    ) : GameEvents()
}
