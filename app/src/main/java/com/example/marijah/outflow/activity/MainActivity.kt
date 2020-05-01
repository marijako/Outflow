package com.example.marijah.outflow.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marijah.outflow.R

import com.example.marijah.outflow.helpers.HelperManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HelperManager.setDefaultFont(this, "MONOSPACE", "oswald_light.ttf")
        setContentView(R.layout.activity_main)
    }
}
