package com.edvora.draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import com.edvora.utils.Constants
import java.util.ArrayList


class DrawCircleCanvas(context: Context?) : View(context) {
    private val brush = Paint(Paint.ANTI_ALIAS_FLAG)
    var startX = 0f
    var startY = 0f
    var endX = 0f
    var endY = 0f
    private val shape = ArrayList<RectF>()
    private val colorList = ArrayList<Int>()

    //drawing using canvas from Arraylist
    override fun onDraw(canvas: Canvas) {
        canvas.drawOval(startX, startY, endX, endY, brush)
        for (i in shape.indices) {
            brush.color = colorList[i]
            canvas.drawOval(shape[i], brush)
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
                val rectF = RectF(startX, startY, endX, endY)
                shape.add(rectF)
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
