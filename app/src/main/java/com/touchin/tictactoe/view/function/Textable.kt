package com.touchin.tictactoe.view.function

import androidx.annotation.StringRes

interface Textable {
    fun setText(text: String?)
    fun setText(@StringRes textRes: Int)
}
