package com.edvora.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.edvora.utils.Constants
import java.util.ArrayList


class DrawArrowCanvas : View {
    private var drawingLinePath: ArrayList<Path>? = null
    private var drawingArrowPath: ArrayList<Path>? = null
    private var drawingLinePaint: ArrayList<Paint>? = null
    private var pathIndex = 0
    private var startX = -1f
    private var startY = -1f
    private var mX = -1f
    private var mY = -1f
    var arrowLength = 34
    var arrowWidth = 18
    var strokeWidth = Constants.strokeWidth
    private val brush = Paint(Paint.ANTI_ALIAS_FLAG)
    private val colorList = ArrayList<Int>()


    constructor(context: Context?) : super(context) {
        initPath()
    }

    //initializing Paint
    private fun initPaint(): Paint {
        brush.isAntiAlias = true
        brush.isDither = true
        brush.style = Paint.Style.FILL_AND_STROKE
        brush.strokeJoin = Paint.Join.ROUND
        brush.strokeCap = Paint.Cap.ROUND
        brush.strokeWidth = strokeWidth.toFloat()
        return brush
    }

    //initializing and adding Paths.
    private fun initPath() {
        colorList.add(Constants.selectedColor)
        drawingLinePath = ArrayList()
        drawingArrowPath = ArrayList()
        drawingLinePath!!.add(Path())
        drawingArrowPath!!.add(Path())
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

            drawingLinePath!!.add(createPath(event))
            drawingArrowPath!!.add(createPath(event))
            drawingLinePaint!!.add(initPaint())
            pathIndex++
        }
    }

    //drawing values on invalidate via Canvas. applied loop for dianamic colors and line values.
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (startX > -1 && mX > -1) {
            canvas.drawLine(startX, startY, mX, mY, initPaint())
            drawArrow(canvas)
        }
        for (index in 0 until pathIndex) {
            val path = drawingLinePath!![index]
            val arrow_path = drawingArrowPath!![index]
            val paint = drawingLinePaint!![index]
            if (colorList.size > index) {
                brush.color = colorList[index]
            }
            canvas.drawPath(path, paint)
            canvas.drawPath(arrow_path, paint)
        }
    }

    //drawing arrow on the line end
    private fun drawArrow(canvas: Canvas) {
        val angle =
            calculateAngle(startX.toDouble(), startY.toDouble(), mX.toDouble(), mY.toDouble())
        val final_angle = (180 - angle).toFloat()
        val arrow_path = Path()
        val arrow_matrix = Matrix()
        arrow_matrix.postRotate(final_angle, mX, mY)
        arrow_path.moveTo(mX, mY)
        arrow_path.lineTo(mX - arrowWidth, mY + arrowLength)
        arrow_path.moveTo(mX, mY)
        arrow_path.lineTo(mX + arrowWidth, mY + arrowLength)
        arrow_path.lineTo(mX - arrowWidth, mY + arrowLength)
        arrow_path.transform(arrow_matrix)
        canvas.drawPath(arrow_path, initPaint())
    }

    //saving arrow for to draw on the line end
    private fun saveArrow() {
        if (mX == -1f || mY == -1f) {
            return
        }
        val angle =
            calculateAngle(startX.toDouble(), startY.toDouble(), mX.toDouble(), mY.toDouble())
        val final_angle = (180 - angle).toFloat()
        val arrow_path = drawingArrowPath!![pathIndex - 1]
        val arrow_matrix = Matrix()
        arrow_matrix.postRotate(final_angle, mX, mY)
        arrow_path.moveTo(mX, mY)
        arrow_path.lineTo(mX - arrowWidth, mY + arrowLength)
        arrow_path.moveTo(mX, mY)
        arrow_path.lineTo(mX + arrowWidth, mY + arrowLength)
        arrow_path.lineTo(mX - arrowWidth, mY + arrowLength)
        arrow_path.transform(arrow_matrix)
    }

    //some calculations for angle for line
    fun calculateAngle(x1: Double, y1: Double, x2: Double, y2: Double): Double {
        var angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1))
        angle = angle + Math.ceil(-angle / 360) * 360 //Keep angle between 0 and 360
        return angle
    }

    //Motion touch event listener.
    override fun onTouchEvent(event: MotionEvent): Boolean {
        brush.color = Constants.selectedColor
        when (event.action) {
            MotionEvent.ACTION_UP -> actionUp(event)
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
        mX = event.x
        mY = event.y
    }

    //on starting touch of screen updating some values and saving arrow.
    private fun actionUp(event: MotionEvent) {
        colorList.add(Constants.selectedColor)
        drawingLinePath!![pathIndex - 1].lineTo(event.x, event.y)
        saveArrow()
        startX = -1f
        startY = -1f
        mX = -1f
        mY = -1f
    }
}
