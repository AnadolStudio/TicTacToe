package com.touchin.tictactoe.feature.start

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.touchin.tictactoe.base.viewmodel.BaseActionViewModel
import com.touchin.tictactoe.base.viewmodel.navigateTo
import com.touchin.tictactoe.base.viewmodel.navigateUp
import com.touchin.tictactoe.R
import com.touchin.tictactoe.feature.game.GameFragment.Companion.GAME_TYPE_KEY
import com.touchin.tictactoe.feature.game.GameType

class StartViewModel : BaseActionViewModel(), StartController {

    override fun onDuoClicked() = navigateToGame(GameType.DUO)
    override fun onTrioClicked() = showTodo()

    private fun navigateToGame(gameType: GameType) {
        _navigationEvent.navigateTo(
            id = R.id.action_startFragment_to_gameFragment,
            args = bundleOf(GAME_TYPE_KEY to gameType)
        )
    }

    override fun onBackClicked() = _navigationEvent.navigateUp()

    @Suppress("UNCHECKED_CAST")
    class Factory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StartViewModel() as T
        }
    }

}
