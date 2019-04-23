package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.marijah.outflow.R
import com.example.marijah.outflow.adapters.ConnectionsAdapter
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Invitation
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_connections.*

class ConnectionsActivity : Activity() {

    private val arrayListOfConnections: ArrayList<Invitation> = ArrayList()

    private lateinit var mAdapter : ConnectionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connections)

        // uspostavljanje veze sa fajrbejs bazom
        val database = FirebaseDatabase.getInstance()
        // proveravamo korisnikove konekcije
        val myReferenceToConnections = database.reference.child("connections_for_${AppManager.getInstance(this).currentlyLoggedInUserEmail}")
        val childEventListenerForConnections = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val invitationItem = dataSnapshot.getValue(Invitation::class.java)
                if (invitationItem != null)
                    arrayListOfConnections.add(invitationItem)

                mAdapter.notifyDataSetChanged()
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

        myReferenceToConnections.addChildEventListener(childEventListenerForConnections)


        mAdapter = ConnectionsAdapter(this, arrayListOfConnections)
        // Connect the mAdapter with the recycler view.
        recyclerViewConnections.adapter = mAdapter
        val layoutManager = LinearLayoutManager(this)
        // Give the recycler view a default layout manager.
        recyclerViewConnections.layoutManager = layoutManager

    }
}
