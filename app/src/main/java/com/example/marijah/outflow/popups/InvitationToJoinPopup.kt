package com.example.marijah.outflow.popups

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Group
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.popup_invitation_to_join.*

class InvitationToJoinPopup(context: Context, private val groupItem: Group) : DimmedPopupDialog(context as Activity, R.layout.popup_invitation_to_join) {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        txtViewGroupName.text = groupItem.groupName
        txtViewGroupCode.text = groupItem.key


        setLayoutAndListeners()

    }

    private fun setLayoutAndListeners() {

        txtViewDeny.setOnClickListener {

            deleteThisRequest()
            showToast(context, "Request successfully denied.")
            dismiss()
        }

        txtViewConfirm.setOnClickListener {
            // ako je korisnik prihvatio ubacujemo ga u grupu

            // trenutnom korisniku ubacujemo tu grupu u njegovu listu
            val myReferenceToMyGroups = database.reference.child("groups_for_${AppManager.getInstance(context).currentlyLoggedInUserEmail}")
            myReferenceToMyGroups.child(groupItem.key).setValue(groupItem)

             // menjamo mu trenutno osluskivanu tabelu troskova
             AppManager.getInstance(context).currentlyLookedTableName = groupItem.key

            // brisemo ovaj poziv iz liste
            deleteThisRequest()



            showToast(context, "You have joined ${groupItem.groupName}")
            dismiss()


        }

    }


    private fun deleteThisRequest() {

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myReferenceToInvitations: DatabaseReference = database.reference.child("invitations_for_${AppManager.getInstance(context).currentlyLoggedInUserEmail}")
        myReferenceToInvitations.child(groupItem.key).removeValue()

    }

}