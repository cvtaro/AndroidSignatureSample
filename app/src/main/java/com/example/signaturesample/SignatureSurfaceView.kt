package com.example.signaturesample

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.os.Environment
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.graphics.get
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

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
        Log.d("[dbg]", "surfaceCreated()")
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
        Log.d("[dbg]", "surfaceChanged()")
        if (mPrevBitmap == null) {
            mPrevBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
    }
    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d("[dbg]", "surfaceDestroyed()")
        mPrevBitmap?.recycle()
//        mPrevBitmap = null
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

    fun saveSignature() {
        if (mPrevBitmap != null) {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val file = File(dir, "pic.jpg")

            try {
                val ops = FileOutputStream(file)
                val res1 = mPrevBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, ops)
                ops.flush()
                ops.close()

                Log.d("[dbg]", "dir = $dir")
                Log.d("[dbg]", "res1 = $res1")
                Log.d("[dbg]", "bmp = ${mPrevBitmap!!.byteCount}")
                Log.d("[dbg]", "bmp1  = ${mPrevBitmap!!.get(1,1)}")
                Log.d("[dbg]", "bmp10 = ${mPrevBitmap!!.get(10,10)}")

                // ギャラリー登録
                val contentValue = ContentValues().apply {
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                    put("_data", file.absolutePath)
                }
                val res = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValue)
                Log.d("[dbg]", "mPrevBitmap = ${mPrevBitmap?.height}, ${mPrevBitmap?.width}")
                Log.d("[dbg]", "res = $res")
            } catch (e: Exception) {
                Log.d("[dbg]", "Exception : out")
                e.printStackTrace()
            }

        }
    }
}