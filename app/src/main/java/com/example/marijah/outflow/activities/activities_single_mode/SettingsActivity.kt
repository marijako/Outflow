package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.example.marijah.outflow.R
import com.example.marijah.outflow.activities.HomeActivity
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.popups.InvitePopup
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



        txtViewInvitePeople.setOnClickListener{

            val invitePopup = InvitePopup(this, R.layout.popup_invite)
            invitePopup.show()

        }

        txtViewSignOut.setOnClickListener {

            AuthUI.getInstance().signOut(this)

           /* intent = Intent(this, LoginSingleActivity::class.java)
            startActivity(intent)*/

            finish()

            intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        txtViewChangeMode.setOnClickListener {
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        HelperManager.setTypefaceRegular(assets, txtViewName)
        HelperManager.setTypefaceRegular(assets, txtViewSignOut)
        HelperManager.setTypefaceRegular(assets, txtViewChangeMode)


    }

    
}
