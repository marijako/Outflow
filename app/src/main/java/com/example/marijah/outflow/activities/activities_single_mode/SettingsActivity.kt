package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.marijah.outflow.R
import com.example.marijah.outflow.activities.HomeActivity
import com.example.marijah.outflow.helpers.HelperManager
import kotlinx.android.synthetic.main.activity_settings.*

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
                imgViewReceiveNotificationsSwitch.setImageDrawable(resources.getDrawable(R.drawable.switch_on))
            } else
                imgViewReceiveNotificationsSwitch.setImageDrawable(resources.getDrawable(R.drawable.switch_off))
        }

        txtViewPassword.setOnClickListener {
            var intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        imgViewPasswordSwitch.setOnClickListener {
            i++
            if (i % 2 == 0) {
                imgViewPasswordSwitch.setImageDrawable(resources.getDrawable(R.drawable.switch_on))

            } else
                imgViewPasswordSwitch.setImageDrawable(resources.getDrawable(R.drawable.switch_off))
        }

        txtViewSignOut.setOnClickListener {
            intent = Intent(this, LoginSingleActivity::class.java)
            startActivity(intent)
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

    }
}
