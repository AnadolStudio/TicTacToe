package com.touchin.tictactoe.feature.game

import com.anadolstudio.core.viewmodel.BaseController

interface GameController : BaseController {
    fun onItemClicked(row: Int, column: Int)
    fun restart()
}
