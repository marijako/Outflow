package com.example.marijah.outflow.popups

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.HelperManager.setUpNewTableName
import com.example.marijah.outflow.helpers.categoryPickedObject
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.ExpenseItem
import com.example.marijah.outflow.models.Invitation
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_new_entry.*
import kotlinx.android.synthetic.main.popup_invitation.*

class InvitationPopup(activity: Activity, private val emailThatsInvining: String, private val key: String) : DimmedPopupDialog(activity, R.layout.popup_invitation) {

    private lateinit var database: FirebaseDatabase
    private lateinit var myReferenceToInvitations: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance()
        myReferenceToInvitations = database.reference.child("invitations_for_${AppManager.getInstance(context).currentlyLoggedInUserEmail}")

        txtViewUsername.text = emailThatsInvining

        txtViewConfirm.setOnClickListener {
            // ako je korisnik pristao, spajamo tabele i brisemo korisnika
            showToast(context, "Request accepted")

            // TODO - SPAJANJE TABELE I UBACIVANJE U LISTU KONEKCIJA

            // dodavanje u listu konekcija
            val myReferenceToConnections = database.reference.child("connections_for_${AppManager.getInstance(context).currentlyLoggedInUserEmail}")

            val invitationItemKey: String = myReferenceToConnections.push().key ?: " "
            val invitationForm = Invitation(invitationItemKey, emailThatsInvining)
            myReferenceToConnections.child(invitationItemKey).setValue(invitationForm)

            val arrayListOfConnections: ArrayList<Invitation> = ArrayList()

            val childEventListenerForConnections = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                    val invitationItem = dataSnapshot.getValue(Invitation::class.java)

                    //val invitingEmail = dataSnapshot.getValue(String::class.java)

                    if (invitationItem != null) {
                        arrayListOfConnections.add(invitationItem)
                        Log.i("Marija", invitationItem.email)
                    }
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

            myReferenceToInvitations.addChildEventListener(childEventListenerForConnections)

            // takodje dodajemo i u listu konekcija korisnika koji je poslao zahtev
            val friendsReferenceToConnections = database.reference.child("connections_for_${emailThatsInvining}")
            val friendsInvitationItemKey: String = friendsReferenceToConnections.push().key ?: " "
            val friendsInvitationForm = Invitation(friendsInvitationItemKey, AppManager.getInstance(context).currentlyLoggedInUserEmail)
            friendsReferenceToConnections.child(invitationItemKey).setValue(friendsInvitationForm)


            // spajanje tabela
            // naziv za spajanje tabela je sledeci expenses_ +sve adrese poredjane po abecednom redu

            AppManager.getInstance(context).currentlyLookedTableName = setUpNewTableName(AppManager.getInstance(context).currentlyLoggedInUserEmail, emailThatsInvining)
            val newReferenceToMergedDatabase = database.reference.child(AppManager.getInstance(context).currentlyLookedTableName)

            val expenseItemID: String = newReferenceToMergedDatabase.push().key ?: " "

            // pravimo objekat sa svim potrebnim informacijama
            val expense = ExpenseItem(expenseItemID, Integer.parseInt("76757"),
                    "pozzz",
                    "pozzz",
                    "pozzz",
                    "pozzz")

            // dodajemo taj objekat u bazu
            newReferenceToMergedDatabase.child(expenseItemID).setValue(expense)




            removeValue()
            dismiss()
        }

        txtViewDeny.setOnClickListener {
            // ako je korisnik odbio i brisemo korisnika
            showToast(context, "Request denied")

            removeValue()
            dismiss()
        }
    }

    private fun removeValue() {
        myReferenceToInvitations.child(key).removeValue()
    }


    override fun onBackPressed() {

    }



    /*fun setUpNewTableName(vararg emails: String): String
    {

        for (i in 0 to emails.size)
        {

        }

    }*/

}
