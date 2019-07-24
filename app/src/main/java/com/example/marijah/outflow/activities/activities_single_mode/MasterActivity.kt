package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Invitation
import com.example.marijah.outflow.popups.InvitationPopup
import com.google.firebase.database.*

/**
 * Master klasa svih aktivitija
 * U njoj osluskujemo poziv za prijateljstvo
 */
open class MasterActivity : Activity() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Marija", "OnCreate() MasterActivity!")

    }

   /* fun listenToTheInvitations() {

        if (AppManager.getInstance(this).currentlyLoggedInUserEmail != "") {

            val myReferenceToInvitations: DatabaseReference = database.reference.child("invitations_for_${AppManager.getInstance(this).currentlyLoggedInUserEmail}")

            if (AppManager.getInstance(this).userEnteredInAppForTheFirstTime) {
                val invitationItemKey: String = myReferenceToInvitations.push().key ?: " "
                val invitationForm = Invitation(invitationItemKey, AppManager.getInstance(this).currentlyLoggedInUserEmail)
                myReferenceToInvitations.child(invitationItemKey).setValue(invitationForm)

                AppManager.getInstance(this).setUserEnteredInAppForTheFirstTime()
            }

            val childEventListenerForExpenses = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                    val invitationItem = dataSnapshot.getValue(Invitation::class.java)

                    //val invitingEmail = dataSnapshot.getValue(String::class.java)

                    if (invitationItem != null)
                      //  callTheInvitationPopup(invitationItem.email, invitationItem.key)
                    //showToast(applicationContext, "You are invited $invitingEmail")
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

            myReferenceToInvitations.addChildEventListener(childEventListenerForExpenses)
        }
    }
*/
/*
    private fun callTheInvitationPopup(email: String, key: String) {
        val invitationPopup = InvitationPopup(this, email, key)
        invitationPopup.show()

    }*/

}