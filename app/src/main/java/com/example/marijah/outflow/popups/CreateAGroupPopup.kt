package com.example.marijah.outflow.popups

import android.app.Activity
import android.os.Bundle
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Group
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.popup_create_a_group.*

class CreateAGroupPopup(private var activity: Activity, dialogLayoutResourceID: Int) : DimmedPopupDialog(activity, dialogLayoutResourceID) {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var myReferenceToGroups: DatabaseReference
    lateinit var groupKey: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        myReferenceToGroups = database.reference.child("groups_of_the_app")

        groupKey = myReferenceToGroups.push().key ?: " "
        txtViewInvitationCode.text = groupKey


        setLayoutAndListeners()


    }

    private fun setLayoutAndListeners() {


        txtViewCreateAGroup.setOnClickListener {

            if (!editTextGroupName.text.isEmpty()) {

                // ubacujemo grupu u listu grupa
                val groupItem = Group(txtViewInvitationCode.text.toString(), editTextGroupName.text.toString(), arrayListOf("${AppManager.getInstance(context).currentlyLoggedInUserEmail}"))
                myReferenceToGroups.child(txtViewInvitationCode.text.toString()).setValue(groupItem)

                // trenutnom korisniku ubacujemo tu grupu u njegovu listu
                val myReferenceToMyGroups = database.reference.child("groups_for_${AppManager.getInstance(context).currentlyLoggedInUserEmail}")
                myReferenceToMyGroups.child(groupKey).setValue(groupItem)

                // menjamo mu trenutno osluskivanu tabelu troskova
                AppManager.getInstance(context).currentlyLookedTableName = groupKey


                showToast(activity, context.getString(R.string.group_successfully_created))
                dismiss()





            } else {
                showToast(activity, context.getString(R.string.empty_field))
            }
        }
    }


}
