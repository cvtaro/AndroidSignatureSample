package com.example.signaturesample

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class SignatureSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SurfaceView(context, attrs) ,SurfaceHolder.Callback{

    private var mPaint: Paint
    private var mPath: Path? = null
    private var mColor: Int

    private var mPrevCanvas: Canvas? = null
    private var mPrevBitmap: Bitmap? = null

    private var mSurfaceHolder: SurfaceHolder? = null

    init {
        mSurfaceHolder = holder

        // 透過設定
        setZOrderOnTop(true)
        mSurfaceHolder?.setFormat(PixelFormat.TRANSPARENT)

        mSurfaceHolder?.addCallback(this)

        // ペイントの設定
        mPaint = Paint()
        mColor = Color.BLACK
        mPaint.color = mColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 10f
    }

    //
    //          SurfaceHolder.Callback
    //
    override fun surfaceCreated(p0: SurfaceHolder) {
        if (mPrevBitmap == null) {
            mPrevBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        if (mPrevCanvas == null) {
            mPrevCanvas = Canvas(mPrevBitmap!!)
        }
        mPrevCanvas?.drawColor(0, PorterDuff.Mode.CLEAR)
    }
    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        // no process
    }
    override fun surfaceDestroyed(p0: SurfaceHolder) {
        mPrevBitmap?.recycle()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x = event.x
        var y = event.y
        Log.d("[dbg]", "x = $x, y = $y, action = ${event.action}")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPath = Path()
                mPath?.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                mPath?.lineTo(x, y)
                drawLine(mPath!!)  // 描画
            }
            MotionEvent.ACTION_UP -> {
                mPath?.lineTo(x, y)
                drawLine(mPath!!) // 描画
                mPrevCanvas!!.drawPath(mPath!!, mPaint)
            }
        }
        return true
    }
    private fun drawLine(path: Path) {
        val canvas = mSurfaceHolder!!.lockCanvas()          // カンバスロック
        canvas.drawColor(0, PorterDuff.Mode.CLEAR)    // クリア
        // 前回のBitMapを描画してからパスを描画する
        canvas.drawBitmap(mPrevBitmap!!, 0f, 0f, null)
        canvas.drawPath(path, mPaint)
        mSurfaceHolder!!.unlockCanvasAndPost(canvas)          // カンバスロック解除
    }
}