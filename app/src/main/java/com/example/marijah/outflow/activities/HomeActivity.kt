package com.example.marijah.outflow.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.marijah.outflow.R
import com.example.marijah.outflow.activities.activities_single_mode.ChangePasswordActivity
import com.example.marijah.outflow.activities.activities_single_mode.MasterActivity
import com.example.marijah.outflow.activities.activities_single_mode.NewEntryActivity
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.popups.InvitationPopup
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : MasterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setLayoutAndListeners()

    }


    // setting up the listeners on our activity
    private fun setLayoutAndListeners() {

        imgViewTrackYourExpensesButton.setOnClickListener {
            intent = Intent(this, NewEntryActivity::class.java)
            startActivity(intent)
        }



        imgViewTrackGroupExpensesButton.setOnClickListener {

            val invitationPopup = InvitationPopup(this, R.layout.popup_invitation)
            invitationPopup.show()

        }

        imgViewInvitedButton.setOnClickListener {
            intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        // postavljamo font
        HelperManager.setTypefaceRegular(assets, txtViewTrackYourExpensesButton)
        HelperManager.setTypefaceRegular(assets, txtViewTrackGroupExpensesButton)
        HelperManager.setTypefaceRegular(assets, txtViewInvitedButton)


    }

}
