package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import com.example.marijah.outflow.R
import com.example.marijah.outflow.activities.HomeActivity
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Group
import com.example.marijah.outflow.models.Invitation
import com.example.marijah.outflow.popups.InvitationPopup
import com.example.marijah.outflow.popups.CreateAGroupPopup
import com.example.marijah.outflow.popups.InvitationToJoinPopup
import com.example.marijah.outflow.popups.JoinAGroupPopup
import com.firebase.ui.auth.AuthUI
import com.google.firebase.database.*
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
                imgViewReceiveNotificationsSwitch.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.switch_on))
            } else
                imgViewReceiveNotificationsSwitch.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.switch_off))
        }

        /*  txtViewPassword.setOnClickListener {
              val intent = Intent(this, ChangePasswordActivity::class.java)
              startActivity(intent)
          }*/

        txtViewCheckYourGroups.setOnClickListener {
            val intent = Intent(this, ListOfGroupsActivity::class.java)
            startActivity(intent)
        }

        imgViewPasswordSwitch.setOnClickListener {
            i++
            if (i % 2 == 0) {
                imgViewPasswordSwitch.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.switch_on))

            } else
                imgViewPasswordSwitch.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.switch_off))
        }



        txtViewCreateAGroup.setOnClickListener {

            val createAGroupPopup = CreateAGroupPopup(this, R.layout.popup_create_a_group)
            createAGroupPopup.show()

        }


        txtViewJoinAGroup.setOnClickListener {

            val joinAGroupPopup = JoinAGroupPopup(this, R.layout.popup_join_a_group)
            joinAGroupPopup.show()

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


        txtViewCheckYourInvites.setOnClickListener {
            // proveravamo da li su pristigli novi zahtevi za mrdzovanje

            var doUserHaveAnyNewInvites = false

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myReferenceToInvitations: DatabaseReference = database.reference.child("invitations_for_${AppManager.getInstance(this).currentlyLoggedInUserEmail}")

            val childEventListenerForInvitations = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                    doUserHaveAnyNewInvites = true

                    val groupItem = dataSnapshot.getValue(Group::class.java)

                    if (groupItem != null)
                       callTheInvitationPopup(groupItem)


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

            myReferenceToInvitations.addChildEventListener(childEventListenerForInvitations)


            val handler = Handler()
            handler.postDelayed(Runnable {
                if (!doUserHaveAnyNewInvites) {
                    showToast(this, getString(R.string.no_new_requests))
                    myReferenceToInvitations.removeEventListener(childEventListenerForInvitations)

                }
            }, 1500)   //5 seconds


        }


    }


    private fun callTheInvitationPopup(groupItem : Group) {
        val invitationToJoinPopup = InvitationToJoinPopup(this, groupItem)
        invitationToJoinPopup.show()

    }


}
