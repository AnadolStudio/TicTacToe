package com.touchin.tictactoe.feature.game

import android.content.res.ColorStateList
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.annotation.StringRes
import androidx.navigation.fragment.navArgs
import com.anadolstudio.core.view.animation.AnimateUtil
import com.anadolstudio.core.view.animation.AnimateUtil.showTranslationStartOutEndIn
import com.anadolstudio.core.view.animation.AnimateUtil.showTranslationTopOutTopIn
import com.anadolstudio.core.viewbinding.viewBinding
import com.anadolstudio.core.viewmodel.livedata.SingleEvent
import com.anadolstudio.core.viewmodel.obtainViewModel
import com.touchin.tictactoe.R
import com.touchin.tictactoe.base.fragment.BaseContentFragment
import com.touchin.tictactoe.databinding.FragmentGameBinding

class GameFragment : BaseContentFragment<GameState, GameViewModel, GameController>(R.layout.fragment_game) {

    companion object {
        const val GAME_TYPE_KEY = "type"
        const val RENDER_ENABLE_KEY = "RENDER_ENABLE_KEY"
    }

    private val binding: FragmentGameBinding by viewBinding { FragmentGameBinding.bind(requireView()) }
    private val args: GameFragmentArgs by navArgs()

    override fun createViewModel(): GameViewModel = obtainViewModel(GameViewModel.Factory(args.type))

    override fun initView(controller: GameController) {
        binding.field.initGrid(args.type.gridSide)
        binding.field.setOnClickListener { row, column -> controller.onItemClicked(row, column) }
    }

    override fun handleEvent(event: SingleEvent) = when (event) {
        GameEvents.Restart -> binding.field.restart()
        is GameEvents.Win -> showWinnerLine(event)
        else -> super.handleEvent(event)
    }

    private fun showWinnerLine(event: GameEvents.Win) {
        binding.field.showWinner(event.player.color, event.startColumn, event.startRow, event.endColumn, event.endRow)
    }

    private fun updateHeader(@StringRes textWithArg: Int, player: Player, isEnd: Boolean) {
        val action = {
            binding.currentPlayerTitle.text = createTitle(textWithArg, player)
            setCurrentShape(player.shapeDrawableRes, player.color)
        }

        if (binding.currentPlayerTitle.text == null) {
            action.invoke()

            return
        }
        if (isEnd) {
            binding.header.showTranslationTopOutTopIn(AnimateUtil.DURATION_SHORT) { action.invoke() }
        } else {
            binding.header.showTranslationStartOutEndIn(AnimateUtil.DURATION_SHORT) { action.invoke() }
        }

    }

    private fun setCurrentShape(shapeDrawableRes: Int, color: Int) {
        binding.currentPlayerShape.setImageResource(shapeDrawableRes)
        binding.currentPlayerShape.imageTintList = ColorStateList.valueOf(color)
    }

    override fun render(state: GameState, controller: GameController) = with(binding) {
        renderHeader(state)
        state.players.forEach { player -> field.setImages(player.shapeDrawableRes, player.color, player.points) }

        state.isEnd.render(RENDER_ENABLE_KEY) {
            field.isEnabled = !state.isEnd
        }
    }

    private fun renderHeader(state: GameState) = when {
        state.isEnd && state.winnerPlayer != null -> {
            updateHeader(R.string.current_player_win, state.winnerPlayer, true)
        }

        state.isEnd -> binding.header.showTranslationTopOutTopIn(AnimateUtil.DURATION_SHORT) {
            binding.currentPlayerTitle.setText(R.string.draw)
            binding.currentPlayerShape.setImageDrawable(null)
        }

        else -> {
            updateHeader(R.string.current_player_turn, state.currentPlayer, false)
        }
    }

    private fun createTitle(@StringRes textWithArg: Int, player: Player): SpannableStringBuilder =
        SpannableStringBuilder().apply {
            val textSpannable = SpannableString(getString(textWithArg, player.name))
            val startIndex = textSpannable.indexOf(player.name)

            textSpannable.setSpan(
                ForegroundColorSpan(player.color),
                startIndex,
                startIndex + player.name.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            append(textSpannable)
        }
}
