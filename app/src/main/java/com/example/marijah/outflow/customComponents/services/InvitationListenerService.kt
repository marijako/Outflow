package com.example.marijah.outflow.customComponents.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.google.firebase.database.*


class InvitationListenerService : Service() {

    /**
     * The system calls this method when another component, such as an activity, requests
     * that the service be started, by calling startService(). If you implement this method,
     * it is your responsibility to stop the service when its work is done, by calling stopSelf()
     * or stopService() methods.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("Marija", "Service Started")
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * This call is required to perform one-time set-up.
     */
    override fun onCreate() {
        super.onCreate()
        Log.i("Marija", "Service onCreate")

        AppManager.getInstance(this).currentlyLoggedInUserEmail

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myReferenceToExpenses: DatabaseReference = database.reference.child("invitations_for_${AppManager.getInstance(this).currentlyLoggedInUserEmail}")

        if (AppManager.getInstance(this).userEnteredInAppForTheFirstTime) {
            myReferenceToExpenses.push().setValue("pozdrav")

            AppManager.getInstance(this).setUserEnteredInAppForTheFirstTime()
        }

        val childEventListenerForExpenses = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val invitingEmail = dataSnapshot.getValue(String::class.java)

                if (invitingEmail != null)
                //callTheActivity(invitingEmail)
                    showToast(applicationContext, "You are invited $invitingEmail")

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {


            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        myReferenceToExpenses.addChildEventListener(childEventListenerForExpenses)


    }

    override fun onDestroy() {
        Log.i("Marija", "Service Destroy")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private fun callTheActivity(email: String) {
        /* val invitationPopup = InvitationPopup(application, email)
         invitationPopup.show()*/

    }

}