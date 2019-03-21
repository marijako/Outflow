package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.example.marijah.outflow.R
import com.example.marijah.outflow.activities.HomeActivity
import com.example.marijah.outflow.helpers.HelperManager
import com.firebase.ui.auth.AuthUI
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_settings.*
import com.github.mikephil.charting.data.LineData



class SettingsActivity : Activity() {

    var i = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setLayoutAndListeners()
    }

    private fun setLayoutAndListeners() {


        txtViewReceiveDailyNotifications.setOnClickListener {
            i++
            if (i % 2 == 0) {
                imgViewReceiveNotificationsSwitch.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.switch_on))
            } else
                imgViewReceiveNotificationsSwitch.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.switch_off))
        }

        txtViewPassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        imgViewPasswordSwitch.setOnClickListener {
            i++
            if (i % 2 == 0) {
                imgViewPasswordSwitch.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.switch_on))

            } else
                imgViewPasswordSwitch.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.switch_off))
        }

        txtViewSignOut.setOnClickListener {

            AuthUI.getInstance().signOut(this)

           /* intent = Intent(this, LoginSingleActivity::class.java)
            startActivity(intent)*/
            finish()
        }

        txtViewChangeMode.setOnClickListener {
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        HelperManager.setTypefaceRegular(assets, txtViewName)
        HelperManager.setTypefaceLight(assets, txtViewReceiveDailyNotifications)
        HelperManager.setTypefaceLight(assets, txtViewPassword)
        HelperManager.setTypefaceRegular(assets, txtViewSignOut)
        HelperManager.setTypefaceRegular(assets, txtViewChangeMode)

        val entries = ArrayList<Entry>()

       // for (data in dataObjects) {

            // turn your data into Entry objects
        entries.add(Entry(200f, 150f))
        entries.add(Entry(220f, 120f))
        entries.add(Entry(230f, 160f))

        val dataSet = LineDataSet(entries, "Label") // add entries to data set
        dataSet.color = Color.RED
        dataSet.setDrawFilled(true)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.fillColor = Color.CYAN
        dataSet.color = Color.CYAN
        dataSet.fillAlpha = 255
        dataSet.setDrawCircles(false)

        dataSet.valueTextColor = Color.YELLOW

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
    }

    
}
