package com.touchin.tictactoe.util.confetti

import android.graphics.Rect
import android.view.ViewGroup
import com.github.jinatonic.confetti.ConfettiManager
import com.github.jinatonic.confetti.ConfettiSource
import com.github.jinatonic.confetti.ConfettoGenerator
import com.github.jinatonic.confetti.Utils
import com.touchin.tictactoe.R

class ConfettiGenerator(confettiContainer: ViewGroup) : BaseConfettiGenerator(confettiContainer) {

    override fun getAllConfettiParticlesResId(): List<Int> = listOf(
        R.drawable.img_y0,
        R.drawable.img_y1,
        R.drawable.img_y2,
        R.drawable.img_y3,
        R.drawable.img_p1,
        R.drawable.img_p2,
        R.drawable.img_b0,
        R.drawable.img_b1,
        R.drawable.img_b2,
        R.drawable.img_b3,
        R.drawable.img_g0,
        R.drawable.img_g1,
        R.drawable.img_g2,
        R.drawable.img_g3
    )

    override fun getConfiguredConfettiManager(
        confettiContainer: ViewGroup,
        confettiGenerator: ConfettoGenerator
    ): ConfettiManager {
        val confettiXStartCoord = -100
        val confettiXEndCoord = confettiContainer.width - confettiXStartCoord
        val confettiYStartCoord = -400
        val confettiSource =
            ConfettiSource(confettiXStartCoord, confettiYStartCoord, confettiXEndCoord, 0)

        return ConfettiManager(confettiContainer.context, confettiGenerator, confettiSource, confettiContainer)
            .setEmissionDuration(1500)
            .setEmissionRate(100f)
            .setVelocityY(800f)
            .setRotationalVelocity(180f, 180f)
            .animate()
    }

}
