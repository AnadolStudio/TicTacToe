package com.touchin.tictactoe.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.GridLayout
import android.widget.ImageView
import com.anadolstudio.core.util.common.dpToPx
import com.anadolstudio.core.util.common_extention.getCompatDrawable
import com.anadolstudio.core.view.animation.AnimateUtil.DURATION_NORMAL
import com.touchin.tictactoe.R
import com.touchin.tictactoe.feature.game.GamePoint

class TicTakToeField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr) {

    private companion object {
        const val DEFAULT_SIDE = 0
        val WIN_LINE_WIDTH = 4F.dpToPx()
        val WIN_LINE_BACKGROUND_WIDTH = 8F.dpToPx()
    }

    private var side: Int = DEFAULT_SIDE
    private var clickListener: ((row: Int, column: Int) -> Unit)? = null
    private val buttonList = mutableListOf<ImageView>()
    private var winnerPoints: Rect? = null

    private val gridPaint = Paint().apply {
        color = context.getColor(R.color.colorAccent)
        strokeWidth = WIN_LINE_WIDTH
        style = Paint.Style.STROKE
    }

    private val winLinePaint = Paint().apply {
        strokeWidth = WIN_LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }

    private val winLineBackGroundPaint = Paint().apply {
        strokeWidth = WIN_LINE_BACKGROUND_WIDTH
        color = Color.WHITE
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }

    private fun inflateButtons() {
        buttonList.clear()
        removeAllViews()

        val imageSide = getImageSide().toInt()

        for (i in 0 until side * side) {
            val column = i % side
            val row = i / side
            val imageView = ImageView(context).apply {

                layoutParams = LayoutParams(imageSide, imageSide)
                setBackgroundResource(R.drawable.item_ripple_rectangle)

                setOnClickListener {
                    if (this@TicTakToeField.isEnabled) {
                        clickListener?.invoke(row, column)
                    }
                }
            }

            addView(imageView)
            buttonList.add(imageView)
        }
        layoutParams = layoutParams.apply {
            height = WRAP_CONTENT
        }
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGrid(canvas)
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        val winnerPoints = winnerPoints
        if (canvas != null && winnerPoints != null)
            drawWinLine(
                canvas = canvas,
                startColumn = winnerPoints.left,
                startRow = winnerPoints.top,
                endColumn = winnerPoints.right,
                endRow = winnerPoints.bottom,
            )
    }

    private fun drawGrid(canvas: Canvas) {
        val imageSide = getImageSide()

        for (i in 1 until side) {
            canvas.drawLine(imageSide * i, 0F, imageSide * i, height.toFloat(), gridPaint)
        }

        for (i in 1 until side) {
            canvas.drawLine(0F, imageSide * i, width.toFloat(), imageSide * i, gridPaint)
        }
    }

    private fun drawWinLine(
        canvas: Canvas,
        startColumn: Int,
        startRow: Int,
        endColumn: Int,
        endRow: Int,
    ) {
        val imageSide = getImageSide()

        val startX = (startColumn + 1) * imageSide - (imageSide) / 2
        val startY = (startRow + 1) * imageSide - (imageSide) / 2
        val endX = (endColumn + 1) * imageSide - (imageSide) / 2
        val endY = (endRow + 1) * imageSide - (imageSide) / 2

        canvas.drawLine(startX, startY, endX, endY, winLineBackGroundPaint)
        canvas.drawLine(startX, startY, endX, endY, winLinePaint)
    }

    private fun getImageSide(): Float = (width / side).toFloat()

    fun initGrid(side: Int) {
        this.side = side
        columnCount = side
        rowCount = side

        post {
            inflateButtons()
        }
    }

    fun setOnClickListener(clickListener: ((row: Int, column: Int) -> Unit)?) {
        this.clickListener = clickListener
    }

    fun setImages(shapeDrawableRes: Int, color: Int, points: List<GamePoint>) {
        val drawable = context.getCompatDrawable(shapeDrawableRes)

        points.map { it.toIndex(side) }.forEach { index ->
            buttonList.getOrNull(index)?.apply {
                setImageDrawable(drawable)
                imageTintList = ColorStateList.valueOf(color)
                this.isEnabled = false
            }
        }
    }

    fun restart() {
        isEnabled = true
        winnerPoints = null
        buttonList.forEach {
            it.animate()
                .scaleX(0F)
                .scaleY(0F)
                .setDuration(DURATION_NORMAL)
                .withEndAction { initGrid(side) }
                .start()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        buttonList.forEach { it.isEnabled = isEnabled }
        alpha = if (enabled) 1F else 0.3F
    }

    fun showWinner(color: Int, startColumn: Int, startRow: Int, endColumn: Int, endRow: Int) {
        winLinePaint.color = color
        isEnabled = false
        winnerPoints = Rect(startColumn, startRow, endColumn, endRow)
    }
}
