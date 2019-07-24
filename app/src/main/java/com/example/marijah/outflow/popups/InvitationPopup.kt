package com.example.marijah.outflow.popups

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.HelperManager.replaceTheLastOccurrenceOfTheSubstringInAString
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Group
import com.example.marijah.outflow.models.Invitation
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.popup_invitation.*

class InvitationPopup(context: Context, private val groupName: String, private val groupKey: String, private val groupItem : Group) : DimmedPopupDialog(context as Activity, R.layout.popup_invitation) {

    private lateinit var database: FirebaseDatabase
    private lateinit var myReferenceToInvinitedEmailGroups: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val headerName = "${txtViewHeaderName.text} ${this.groupName}"
        txtViewHeaderName.text = headerName

        setLayoutAndListeners()

    }

    private fun setLayoutAndListeners()
    {

        txtViewCancel.setOnClickListener {
            dismiss()
        }

        txtViewInvite.setOnClickListener {

            if(!editTextInvitingEmail.text.isEmpty() && editTextInvitingEmail.text.contains("@",true))
            {
                // TODO - da ubacimo ovaj group item u invite listu ukucanog mejla

                val emailToInvite = editTextInvitingEmail.text.toString().replace('.','@')

                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                val myReferenceToInvitations: DatabaseReference = database.reference.child("invitations_for_$emailToInvite")
                myReferenceToInvitations.child(groupKey).setValue(groupItem)

                showToast(context, "Invitation sent successfully.")
                dismiss()

            }
            else
            {
                showToast(context, context.getString(R.string.email_error))
            }


        }

    }


}
