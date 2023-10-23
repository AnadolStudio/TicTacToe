package com.touchin.tictactoe.feature.start

import com.anadolstudio.core.viewmodel.BaseController

interface StartController : BaseController {
    fun onDuoClicked()
    fun onTrioClicked()
}
