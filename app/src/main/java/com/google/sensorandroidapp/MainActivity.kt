package com.google.sensorandroidapp

import android.content.ContentValues.TAG
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import java.io.IOException

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var sensor: Sensor? = null
    private var sensorManager: SensorManager? = null
    private lateinit var image: ImageView
    private lateinit var background: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image = findViewById(R.id.displayImage)
        background = findViewById(R.id.background)
        image.visibility = View.INVISIBLE
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (sensor == null) {
            Log.e(TAG, "Light sensor not available")
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        try {
            if (event != null) {
                if (event.values[0] < 30) {
                    image.visibility = View.VISIBLE
                    background.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
                } else {
                    image.visibility = View.INVISIBLE
                    background.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                }
            }
        } catch (e: IOException) {
            Log.d(TAG, "onSensorChanged: ${e.message}")
        }
    }

    override fun onResume() {
        super.onResume()
        sensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No action needed
    }
}
