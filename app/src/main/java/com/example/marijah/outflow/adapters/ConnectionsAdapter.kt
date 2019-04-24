package com.example.marijah.outflow.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.HelperManager.replaceTheLastOccurrenceOfTheSubstringInAString
import com.example.marijah.outflow.models.Invitation
import java.util.*

class ConnectionsAdapter(private val context: Context, private val arrayListOfConnections: ArrayList<Invitation>) : RecyclerView.Adapter<ConnectionsAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtViewConnectedEmail: TextView = itemView.findViewById(R.id.txtViewConnectedEmail)
        var txtViewNumber: TextView = itemView.findViewById(R.id.txtViewNumber)

        init {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionsAdapter.ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_connection, parent, false)

        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ConnectionsAdapter.ViewHolder, position: Int) {

        val itemConnection = arrayListOfConnections[position]
        holder.txtViewNumber.text = (position + 1).toString()
        holder.txtViewConnectedEmail.text = replaceTheLastOccurrenceOfTheSubstringInAString("@",".",itemConnection.email.toString() )


    }


    override fun getItemCount(): Int {
        return arrayListOfConnections.size
    }

}
