package com.edvora

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.edvora.draw.DrawArrowCanvas
import com.edvora.draw.DrawByFingerCanvas
import com.edvora.draw.DrawCircleCanvas
import com.edvora.draw.DrawSquareCanvas
import com.edvora.utils.Constants

class MainActivity : AppCompatActivity() {

    lateinit var pencil: ImageView
    lateinit var arrow: ImageView
    lateinit var square: ImageView
    lateinit var circle: ImageView
    lateinit var colorPicker: ImageView

    lateinit var red: FrameLayout
    lateinit var green: FrameLayout
    lateinit var blue: FrameLayout
    lateinit var black: FrameLayout

    lateinit var clear:TextView

    lateinit var colorsView: ConstraintLayout
    lateinit var drawView: FrameLayout
    lateinit var selectedView: View

    var currentViewIndex = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //calling some methods
        initView()
        initColorView()
        initClickListener()
        initColorClickListener()

        //adding view to Frame Layout for drawing (this is default)
        selectDrawingView(DrawByFingerCanvas(this))

    }

    //initializing views
    private fun initView() {
        pencil = findViewById(R.id.pencil)
        arrow = findViewById(R.id.arrow)
        square = findViewById(R.id.square)
        circle = findViewById(R.id.circle)
        colorPicker = findViewById(R.id.colorPicker)

        colorsView = findViewById(R.id.colorsView)
        drawView = findViewById(R.id.drawView)

        clear = findViewById(R.id.clear)

        //applying default color for drawing
        Constants.selectedColor = resources.getColor(R.color.black)
    }

    //initializing colors
    private fun initColorView() {
        red = findViewById(R.id.red)
        green = findViewById(R.id.green)
        blue = findViewById(R.id.blue)
        black = findViewById(R.id.black)
    }

    //attaching click listeners for tools selection. (applying default color on all, setting tint and bg for selected button, and adding view for drawing)
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initClickListener() {
        pencil.setOnClickListener {
            setDefaultColor()
            pencil.setBackground(getDrawable(R.drawable.tools_selected_bg))
            pencil.setColorFilter(getResources().getColor(R.color.tools_icon_selected))

            currentViewIndex = 1
            selectDrawingView(DrawByFingerCanvas(this))
        }

        arrow.setOnClickListener {
            setDefaultColor()
            arrow.setBackground(getDrawable(R.drawable.tools_selected_bg))
            arrow.setColorFilter(getResources().getColor(R.color.tools_icon_selected))

            currentViewIndex = 2
            selectDrawingView(DrawArrowCanvas(this))
        }

        square.setOnClickListener {
            setDefaultColor()
            square.setBackground(getDrawable(R.drawable.tools_selected_bg))
            square.setColorFilter(getResources().getColor(R.color.tools_icon_selected))

            currentViewIndex = 3
            selectDrawingView(DrawSquareCanvas(this))
        }

        circle.setOnClickListener {
            setDefaultColor()
            circle.setBackground(getDrawable(R.drawable.tools_selected_bg))
            circle.setColorFilter(getResources().getColor(R.color.tools_icon_selected))

            currentViewIndex = 4
            selectDrawingView(DrawCircleCanvas(this))
        }

        colorPicker.setOnClickListener {
            setDefaultColor()
            colorPicker.setBackground(getDrawable(R.drawable.tools_selected_bg))
            colorPicker.setColorFilter(getResources().getColor(R.color.tools_icon_selected))
            colorsView.visibility = View.VISIBLE
        }

        clear.setOnClickListener {
             clearScreen()
        }

    }

    //attaching click listeners for color selection. (applying default color on all colors buttons. then applying bg for selected color. and applying color for drawing)
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initColorClickListener() {
        red.setOnClickListener {
            setColorsDefaultColor()
            red.setBackground(getDrawable(R.drawable.red_color_active_bg))
            Constants.selectedColor = resources.getColor(R.color.red)
        }

        green.setOnClickListener {
            setColorsDefaultColor()
            green.setBackground(getDrawable(R.drawable.green_color_active_bg))
            Constants.selectedColor = resources.getColor(R.color.green)
        }

        blue.setOnClickListener {
            setColorsDefaultColor()
            blue.setBackground(getDrawable(R.drawable.blue_color_active_bg))
            Constants.selectedColor = resources.getColor(R.color.blue)
        }

        black.setOnClickListener {
            setColorsDefaultColor()
            black.setBackground(getDrawable(R.drawable.black_color_active_bg))
            Constants.selectedColor = resources.getColor(R.color.black)
        }

    }

    //applying default color on the color buttons
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setColorsDefaultColor() {
        red.setBackground(getDrawable(R.drawable.red_color_bg))
        green.setBackground(getDrawable(R.drawable.green_color_bg))
        blue.setBackground(getDrawable(R.drawable.blue_color_bg))
        black.setBackground(getDrawable(R.drawable.black_color_bg))

    }

    //applying default tint colors and background color of tools icons
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setDefaultColor() {
        pencil.setBackground(getDrawable(R.drawable.tools_icon_bg))
        arrow.setBackground(getDrawable(R.drawable.tools_icon_bg))
        square.setBackground(getDrawable(R.drawable.tools_icon_bg))
        circle.setBackground(getDrawable(R.drawable.tools_icon_bg))
        colorPicker.setBackground(getDrawable(R.drawable.tools_icon_bg))

        pencil.setColorFilter(getResources().getColor(R.color.tools_icon_default))
        arrow.setColorFilter(getResources().getColor(R.color.tools_icon_default))
        square.setColorFilter(getResources().getColor(R.color.tools_icon_default))
        circle.setColorFilter(getResources().getColor(R.color.tools_icon_default))
        colorPicker.setColorFilter(getResources().getColor(R.color.tools_icon_default))

        colorsView.visibility = View.GONE
    }

    private fun selectDrawingView(view: View) {
        selectedView = view
        drawView.addView(selectedView)
    }

    //clear all view
    private fun clearScreen() {
        drawView.removeAllViews()
        when (currentViewIndex) {

            1 -> {
                selectDrawingView(DrawByFingerCanvas(this))
            }
            2 -> {
                selectDrawingView(DrawArrowCanvas(this))
            }
            3 -> {
                selectDrawingView(DrawSquareCanvas(this))
            }
            4 -> {
                selectDrawingView(DrawCircleCanvas(this))
            }
       }

    }

}