package com.example.signaturesample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class testView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var paint: Paint? = null
    private var path: Path? = null

    init {
        path = Path()
        paint = Paint()
        paint?.color = Color.BLACK
        paint?.style = Paint.Style.STROKE
        paint?.strokeJoin = Paint.Join.ROUND
        paint?.strokeCap = Paint.Cap.ROUND
        paint?.strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(path!!, paint!!)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        Log.d("[dbg]", "x = $x, y = $y, action = ${event.action}")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // mPath = Path()
                path?.moveTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                // mPath = Path()
                path?.lineTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                // mPath = Path()
                path?.lineTo(x, y)
                invalidate()
            }
        }
        return true
    }

}