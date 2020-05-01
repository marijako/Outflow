package com.example.marijah.outflow.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.marijah.outflow.R
import java.util.*

class GroupMembersAdapter(private val context: Context, private val arrayListOfGroupMembers: ArrayList<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<GroupMembersAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var txtViewGroupMember: TextView = itemView.findViewById(R.id.txtViewGroupMember)
        var txtViewNumber: TextView = itemView.findViewById(R.id.txtViewNumber)

        init {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMembersAdapter.ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_group_member, parent, false)


        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: GroupMembersAdapter.ViewHolder, position: Int) {

        val itemMember = arrayListOfGroupMembers[position]
        holder.txtViewNumber.text = (position + 1).toString()
        holder.txtViewGroupMember.text = itemMember


    }


    override fun getItemCount(): Int {
        return arrayListOfGroupMembers.size
    }
}