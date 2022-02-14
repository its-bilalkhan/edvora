package com.edvora.draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import com.edvora.utils.Constants
import java.util.ArrayList


class DrawSquareCanvas(context: Context?) : View(context) {
    private val brush = Paint(Paint.ANTI_ALIAS_FLAG)
    var startX = 0f
    var startY = 0f
    var endX = 0f
    var endY = 0f
    private val rectangles = ArrayList<Rect>()
    private val colorList = ArrayList<Int>()

    //drawing using canvas from Arraylist
    override fun onDraw(c: Canvas) {
        c.drawRect(startX, startY, endX, endY, brush)
        for (i in rectangles.indices) {
            brush.color = colorList[i]
            c.drawRect(rectangles[i], brush)
        }
    }

    //event on motion, getting X, Y values of screen
    override fun onTouchEvent(event: MotionEvent): Boolean {
        brush.color = Constants.selectedColor
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
                val rect = Rect(
                    startX.toInt(),
                    startY.toInt(), endX.toInt(), endY.toInt()
                )
                rectangles.add(rect)
                colorList.add(Constants.selectedColor)
            }
            else -> return false
        }
        return true
    }

    //initializing Paint style
    init {
        brush.style = Paint.Style.STROKE
        brush.strokeWidth = Constants.strokeWidth
    }
}