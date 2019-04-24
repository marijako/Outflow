package com.example.marijah.outflow.popups

import android.app.Activity
import android.os.Bundle
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.HelperManager.replaceTheLastOccurrenceOfTheSubstringInAString
import com.example.marijah.outflow.helpers.HelperManager.setUpNewTableName
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.ExpenseItem
import com.example.marijah.outflow.models.Invitation
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.popup_invitation.*

class InvitationPopup(activity: Activity, private val emailThatsInvining: String, private val key: String) : DimmedPopupDialog(activity, R.layout.popup_invitation) {

    private lateinit var database: FirebaseDatabase
    private lateinit var myReferenceToInvitations: DatabaseReference

    private var arrayListOfConnectionEmails: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // dodajemo i samog korisnika
        arrayListOfConnectionEmails.add(AppManager.getInstance(context).currentlyLoggedInUserEmail)

        database = FirebaseDatabase.getInstance()
        myReferenceToInvitations = database.reference.child("invitations_for_${AppManager.getInstance(context).currentlyLoggedInUserEmail}")

        txtViewUsername.text = replaceTheLastOccurrenceOfTheSubstringInAString("@", ".", emailThatsInvining)

        txtViewConfirm.setOnClickListener {
            // ako je korisnik pristao, spajamo tabele i brisemo korisnika iz liste invajtova
            showToast(context, "Request accepted")


            // dodajemo korisnika u nasu listu konekcija
            addToMyConnections()
            // takodje dodajemo i u listu konekcija korisnika koji je poslao zahtev
            addToMeToMyFriendsConnections()

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


            // sve moje konekcije moram da upisem kod mog prijatelja
            // kod svih mojih prijatelja moram da upisem novu konekciju
            // kod svih mojih prijatelja moram da updejtujem ime tabele


            // i mejl koji me je pozvao bi morao da meni da sve svoje kontakte
            // da njima svima updejtuje tabele

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

    /**
     * Funkcija za dodavanje novog kontakta u moju listu konekcija
     */
    private fun addToMyConnections() {
        // dodavanje u listu mojih konekcija
        val myReferenceToConnections = database.reference.child("connections_for_${AppManager.getInstance(context).currentlyLoggedInUserEmail}")

        val invitationItemKey: String = myReferenceToConnections.push().key ?: " "
        val invitationForm = Invitation(invitationItemKey, emailThatsInvining)
        myReferenceToConnections.child(invitationItemKey).setValue(invitationForm)


        val childEventListenerForConnections = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val invitationItem = dataSnapshot.getValue(Invitation::class.java)
                if (invitationItem != null) {
                    arrayListOfConnectionEmails.add(invitationItem.email)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        myReferenceToInvitations.addChildEventListener(childEventListenerForConnections)
    }


    /**
     * Funkcija za dodavanje mog mejla u listu prijateljskih konekcija
     */
    private fun addToMeToMyFriendsConnections() {

        val friendsReferenceToConnections = database.reference.child("connections_for_$emailThatsInvining")
        val friendsInvitationItemKey: String = friendsReferenceToConnections.push().key ?: " "
        val friendsInvitationForm = Invitation(friendsInvitationItemKey, AppManager.getInstance(context).currentlyLoggedInUserEmail)
        friendsReferenceToConnections.child(friendsInvitationItemKey).setValue(friendsInvitationForm)


    }


    private fun removeValue() {
        myReferenceToInvitations.child(key).removeValue()
    }


    override fun onBackPressed() {

    }

}
