package com.edvora.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.edvora.utils.Constants
import java.util.ArrayList


class DrawByFingerCanvas : View {
    private var drawingLinePath: ArrayList<Path>? = null
    private var drawingLinePaint: ArrayList<Paint>? = null
    private var pathIndex = 0
    private var startX = -1f
    private var startY = -1f
    private var mX = -1f
    private var mY = -1f
    var strokeWidth = Constants.strokeWidth
    private val brush = Paint(Paint.ANTI_ALIAS_FLAG)
    private val colorList = ArrayList<Int>()


    constructor(context: Context?) : super(context) {
        initPath()
    }

    //initializing Paint
    private fun initPaint(): Paint {
        brush.style = Paint.Style.STROKE
        brush.strokeWidth = strokeWidth
        return brush
    }

    //initializing and adding Paths.
    private fun initPath() {
        colorList.add(Constants.selectedColor)
        drawingLinePath = ArrayList()
        drawingLinePath!!.add(Path())
        drawingLinePaint = ArrayList()
        drawingLinePaint!!.add(initPaint())
        pathIndex++
    }

    //creating line
    private fun createPath(event: MotionEvent): Path {
        val path = Path()
        path.moveTo(event.x, event.y)
        return path
    }

    //updating index on leaving touch on screen.
    private fun updateIndex(event: MotionEvent) {
        if (pathIndex == drawingLinePath!!.size) {
            colorList.add(Constants.selectedColor)
            drawingLinePath!!.add(createPath(event))
            drawingLinePaint!!.add(initPaint())
            pathIndex++
        }
    }

    //drawing values on invalidate via Canvas. applied loop for dianamic colors and line values.
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (startX > -1 && mX > -1) {
            canvas.drawLine(startX, startY, mX, mY, initPaint())
        }
        for (index in 0 until pathIndex) {
            val path = drawingLinePath!![index]
            val paint = drawingLinePaint!![index]
            if (colorList.size > index) {
                brush.color = colorList[index]
            }
            canvas.drawPath(path, paint)
        }
    }

    //Motion touch event listener.
    override fun onTouchEvent(event: MotionEvent): Boolean {
        brush.color = Constants.selectedColor
        when (event.action) {
            MotionEvent.ACTION_MOVE -> actionMove(event)
            MotionEvent.ACTION_DOWN -> actionDown(event)
        }
        invalidate()
        return true
    }

    //on leaving touch of screen updating index.
    private fun actionDown(event: MotionEvent) {
        updateIndex(event)
        startX = event.x
        startY = event.y
    }

    //saving motion X, Y values of screen. on moving finger on screen
    private fun actionMove(event: MotionEvent) {
        drawingLinePath!![pathIndex - 1].lineTo(event.x, event.y)
        startX = -1f
        startY = -1f
        mX = -1f
        mY = -1f
    }
}
