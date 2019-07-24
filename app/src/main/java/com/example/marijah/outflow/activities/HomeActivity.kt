package com.example.marijah.outflow.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.marijah.outflow.R
import com.example.marijah.outflow.activities.activities_single_mode.ChangePasswordActivity
import com.example.marijah.outflow.activities.activities_single_mode.MasterActivity
import com.example.marijah.outflow.activities.activities_single_mode.NewEntryActivity
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.helpers.HelperManager.setDefaultFont
import com.example.marijah.outflow.popups.InvitationPopup
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : MasterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDefaultFont(this, "MONOSPACE", "oswald_light.ttf")


        Log.i("Marija", "OnCreate() HomeActivity!")

        setContentView(R.layout.activity_home)


        //setDefaultFont(this, "DEFAULT", "oswald_light.ttf")
        /*setDefaultFont(this, "SERIF", "oswald_light.ttf")*/
        /*setDefaultFont(this, "SANS_SERIF", "oswald_light.ttf")*/

        setLayoutAndListeners()

    }


    // setting up the listeners on our activity
    private fun setLayoutAndListeners() {

        imgViewTrackYourExpensesButton.setOnClickListener {

        }


        imgViewTrackGroupExpensesButton.setOnClickListener {
            intent = Intent(this, NewEntryActivity::class.java)
            startActivity(intent)
        }


        // postavljamo font
        HelperManager.setTypefaceRegular(assets, txtViewTrackYourExpensesButton)
        HelperManager.setTypefaceRegular(assets, txtViewTrackGroupExpensesButton)


    }

}
