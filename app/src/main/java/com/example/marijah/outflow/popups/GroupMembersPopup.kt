package com.example.marijah.outflow.popups

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.marijah.outflow.R
import com.example.marijah.outflow.adapters.GroupMembersAdapter
import kotlinx.android.synthetic.main.popup_group_members.*


class GroupMembersPopup(context: Context, private val arrayListOfGroupMembers: ArrayList<String>) : DimmedPopupDialog(context as Activity, R.layout.popup_group_members) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mAdapter = GroupMembersAdapter(context, arrayListOfGroupMembers)
        recyclerViewGroupMembers.adapter = mAdapter
        val layoutManager = LinearLayoutManager(context)
        recyclerViewGroupMembers.layoutManager = layoutManager

    }
}
