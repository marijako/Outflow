package com.example.marijah.outflow.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.marijah.outflow.R
import com.example.marijah.outflow.activities.activities_group_mode.MasterActivity
import com.example.marijah.outflow.activities.activities_group_mode.NewEntryActivity
import com.example.marijah.outflow.activities.activities_single_mode.NewEntrySingleActivity
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.helpers.HelperManager.setDefaultFont
import com.example.marijah.outflow.models.AppManager
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
            AppManager.getInstance(this).hasUserPickedSingleMode = true
            intent = Intent(this, NewEntrySingleActivity::class.java)
            startActivity(intent)
        }


        imgViewTrackGroupExpensesButton.setOnClickListener {
            AppManager.getInstance(this).hasUserPickedSingleMode = false
            intent = Intent(this, NewEntryActivity::class.java)
            startActivity(intent)
        }


        // postavljamo font
        HelperManager.setTypefaceRegular(assets, txtViewTrackYourExpensesButton)
        HelperManager.setTypefaceRegular(assets, txtViewTrackGroupExpensesButton)


    }

}
