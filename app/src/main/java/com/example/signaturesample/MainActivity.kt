package com.example.signaturesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view1 = findViewById<SignatureView>(R.id.SignatureView)

        val view2 = findViewById<SignatureSurfaceView>(R.id.SignatureSurfaceView)

    }
}