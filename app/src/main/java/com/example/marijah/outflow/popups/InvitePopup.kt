package com.example.marijah.outflow.popups

import android.app.Activity
import android.os.Bundle
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Invitation
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.popup_invite.*

class InvitePopup(private var activity: Activity, dialogLayoutResourceID: Int) : DimmedPopupDialog(activity, dialogLayoutResourceID) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        txtViewSendRequest.setOnClickListener {

            if (!editTextEmail.text.isEmpty() && editTextEmail.text.contains('@', true)) {

                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                val transformedAddress: String = editTextEmail.text.toString().replace('.', '@').toLowerCase()

                /*if (transformedAddress.equals(AppManager.getInstance(activity).currentlyLoggedInUserEmail, true)) {
                    showToast(activity, "You can't invite yourself.")
                    editTextEmail.text.clear()
                } else {*/
                    val myReferenceToInvitations: DatabaseReference = database.reference.child("invitations_for_${transformedAddress}")

                    val invitationItemKey: String = myReferenceToInvitations.push().key ?: " "
                    val invitationForm = Invitation(invitationItemKey, AppManager.getInstance(activity).currentlyLoggedInUserEmail)
                    myReferenceToInvitations.child(invitationItemKey).setValue(invitationForm)

                    showToast(activity, context.getString(R.string.your_invitation_has_been_sent))
                    dismiss()
              //  }
            } else {
                showToast(activity, context.getString(R.string.real_email))
            }


        }

    }

}
