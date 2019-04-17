package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import com.google.firebase.database.FirebaseDatabase

/**
 * Master klasa svih aktivitija
 * U njoj osluskujemo poziv za prijateljstvo
 */
open class MasterActivity : Activity() {

    private val mDatabase = FirebaseDatabase.getInstance().reference

    //

    fun onUserReceivedFriendRequest() {

    }


}