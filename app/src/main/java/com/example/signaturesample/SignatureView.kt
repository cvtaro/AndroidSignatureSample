package com.example.signaturesample

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.io.path.Path

class SignatureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mPath: Path? = null
    private var mPaint: Paint? = null
    private var mBitmap: Bitmap? = null

    init {
        // ペイントの設定
        mPath = Path()
        mPaint = Paint()
        mPaint?.color = Color.BLACK
        mPaint?.style = Paint.Style.STROKE
        mPaint?.strokeCap = Paint.Cap.ROUND
        mPaint?.isAntiAlias = true
        mPaint?.strokeWidth = 10f
        // isFocusable = true
        // isFocusableInTouchMode = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(mPath!!, mPaint!!)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        Log.d("[dbg]", "x = $x, y = $y, action = ${event.action}")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPath?.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                mPath?.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                mPath?.lineTo(x, y)
            }
        }
        invalidate()
        return true
    }


}