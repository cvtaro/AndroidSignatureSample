package com.example.signaturesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import androidx.core.app.ActivityCompat
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var saveButton: Button
    private lateinit var view1: SignatureView
    private lateinit var view2: SignatureSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view1 = findViewById(R.id.SignatureView)

        view2 = findViewById(R.id.SignatureSurfaceView)

        saveButton = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            Log.d("[dag main]", "save button pushed")
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            view2.saveSignature()
        }

    }
}