package com.example.marijah.outflow.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.marijah.outflow.R
import com.example.marijah.outflow.activities.activities_single_mode.ChangePasswordActivity
import com.example.marijah.outflow.activities.activities_single_mode.NewEntryActivity
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.helpers.TAG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : Activity() {

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


        var i = 0
        imgViewTrackGroupExpensesButton.setOnClickListener {

            i++

            // uspostavljanje veze sa fajrbejs bazom
            val database = FirebaseDatabase.getInstance()

            val myRef = database.getReference("entry $i")
            myRef.setValue("Unos broj $i")


            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val value = dataSnapshot.getValue(String::class.java)
                    Log.d(TAG, "Value is: $value")
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

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
