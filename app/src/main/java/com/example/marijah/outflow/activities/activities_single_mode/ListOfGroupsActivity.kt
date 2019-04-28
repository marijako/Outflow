package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.marijah.outflow.R
import com.example.marijah.outflow.adapters.GroupsAdapter
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Group
import com.example.marijah.outflow.models.Invitation
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_list_of_groups.*

class ListOfGroupsActivity : Activity() {

    private val arrayListOfGroups: ArrayList<Group> = ArrayList()

    private lateinit var mAdapter : GroupsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_groups)

        // uspostavljanje veze sa fajrbejs bazom
        val database = FirebaseDatabase.getInstance()
        // proveravamo korisnikove konekcije
        val myReferenceToGroups = database.reference.child("groups_for_${AppManager.getInstance(this).currentlyLoggedInUserEmail}")
        val childEventListenerForConnections = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val groupItem = dataSnapshot.getValue(Group::class.java)
                if (groupItem != null)
                    arrayListOfGroups.add(groupItem)

                mAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

                arrayListOfGroups.clear()

                val groupItem = dataSnapshot.getValue(Group::class.java)
                if (groupItem != null)
                    arrayListOfGroups.add(groupItem)

                mAdapter.notifyDataSetChanged()

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {



                for (group in arrayListOfGroups) {
                    if (group.key == dataSnapshot.key) {
                        arrayListOfGroups.remove(group)
                        mAdapter.notifyDataSetChanged()
                        return
                    }
                }

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        myReferenceToGroups.addChildEventListener(childEventListenerForConnections)


        mAdapter = GroupsAdapter(this, arrayListOfGroups, object : GroupsAdapter.GroupAdapterListener{
            override fun onUserChoseToLeaveTheGroup(groupKey: String) {

                myReferenceToGroups.child(groupKey).removeValue()
                mAdapter.notifyDataSetChanged()
            }
        })
        recyclerViewConnections.adapter = mAdapter
        val layoutManager = LinearLayoutManager(this)
        recyclerViewConnections.layoutManager = layoutManager
    }
}
