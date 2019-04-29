package com.example.marijah.outflow.popups

import android.app.Activity
import android.os.Bundle
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Group
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.popup_join_a_group.*

class JoinAGroupPopup(activity: Activity, dialogLayoutResourceID: Int) : DimmedPopupDialog(activity, dialogLayoutResourceID) {


    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var myReferenceToGroups: DatabaseReference
    private var groupKey: String = ""

    private var arrayListOfGroups: ArrayList<Group> = ArrayList()
    private var groupToJoin: Group? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        myReferenceToGroups = database.reference.child("groups_of_the_app")


        val childEventListenerForConnections = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val groupItem = dataSnapshot.getValue(Group::class.java)
                if (groupItem != null)
                    arrayListOfGroups.add(groupItem)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        myReferenceToGroups.addChildEventListener(childEventListenerForConnections)


        setLayoutAndListeners()


    }

    private fun setLayoutAndListeners() {


        txtViewJoin.setOnClickListener {

            groupKey = editTxtCode.text.toString().trim()

            if (!groupKey.isEmpty() and checkIfAGroupExists(groupKey)) {
                /* TODO - 1) da pokupimo kljuceve svih postojecih grupa
                          2) ako postoji ta grupa dodajemo je u korisnikovu listu grupa
                          3) toj grupi dodajemo novog membera
                          4) gasimo popup*/

                val groupToJoinCopy = groupToJoin

                groupToJoinCopy?.groupMembers?.add(HelperManager.replaceTheLastOccurrenceOfTheSubstringInAString("@", ".", AppManager.getInstance(context).currentlyLoggedInUserEmail))
                myReferenceToGroups.child(groupKey).setValue(groupToJoinCopy)


                val myReferenceToMyGroups = database.reference.child("groups_for_${AppManager.getInstance(context).currentlyLoggedInUserEmail}")
                myReferenceToMyGroups.child(groupKey).setValue(groupToJoinCopy)

                // menjamo mu trenutno osluskivanu tabelu troskova
                AppManager.getInstance(context).currentlyLookedTableName = groupKey

                dismiss()
                showToast(context, "Successfully joined ${groupToJoin!!.groupName}")

            } else {
                showToast(context, "Invalid code.")
            }

        }

    }


    private fun checkIfAGroupExists(enteredKey: String): Boolean {
        for (group in arrayListOfGroups)
            if (group.key == enteredKey) {
                groupToJoin = group
                return true
            }

        return false
    }

}
