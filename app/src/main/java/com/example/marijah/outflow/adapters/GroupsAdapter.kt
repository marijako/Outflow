package com.example.marijah.outflow.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Group
import com.example.marijah.outflow.popups.GroupMembersPopup
import com.example.marijah.outflow.popups.InvitationPopup
import com.example.marijah.outflow.popups.LeaveTheGroupPopup
import java.util.*

class GroupsAdapter(private val context: Context, private val arrayListOfGroups: ArrayList<Group>, private val listener : GroupsAdapter.GroupAdapterListener) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {


    private var previouslySelectedHolder : ViewHolder? = null

    interface GroupAdapterListener
    {
        fun onUserChoseToLeaveTheGroup(groupKey : String)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtViewGroupName: TextView = itemView.findViewById(R.id.txtViewGroupName)
        var txtViewGroupKey: TextView = itemView.findViewById(R.id.txtViewGroupKey)

        var imgViewInviteUsers: ImageView = itemView.findViewById(R.id.imgViewInviteUsers)
        var imgViewLeaveTheGroup: ImageView = itemView.findViewById(R.id.imgViewLeaveTheGroup)

        var imgViewBackground : ImageView = itemView.findViewById(R.id.imgViewBackground)


        var txtViewNumber: TextView = itemView.findViewById(R.id.txtViewNumber)

        init {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsAdapter.ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_group, parent, false)


        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: GroupsAdapter.ViewHolder, position: Int) {

        val itemGroup = arrayListOfGroups[position]
        holder.txtViewNumber.text = (position + 1).toString()
        holder.txtViewGroupName.text = itemGroup.groupName
        holder.txtViewGroupKey.text = itemGroup.key

        holder.imgViewBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_dark_color))
        holder.imgViewLeaveTheGroup.visibility = View.VISIBLE



        holder.txtViewNumber.setOnClickListener {

            showToast(context, "His group contains ${itemGroup.groupMembers.size} members.")

        }

        holder.imgViewInviteUsers.setOnClickListener { callTheInvitationPopup(itemGroup.groupName, itemGroup.key, itemGroup) }


        holder.imgViewLeaveTheGroup.setOnClickListener {
            callAreYouSureThatYouWantToLeaveTheGroup(itemGroup.key)
        }


        if(AppManager.getInstance(context).currentlyLookedTableName == itemGroup.key)
        {
            holder.imgViewBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_pastel_color))
            previouslySelectedHolder = holder
            holder.imgViewLeaveTheGroup.visibility = View.INVISIBLE

        }


        holder.imgViewBackground.setOnClickListener {

            AppManager.getInstance(context).currentlyLookedTableName = itemGroup.key
            previouslySelectedHolder?.imgViewBackground?.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_dark_color))
            previouslySelectedHolder?.imgViewLeaveTheGroup?.visibility = View.VISIBLE

            holder.imgViewLeaveTheGroup.visibility = View.INVISIBLE
            holder.imgViewBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_pastel_color))

            previouslySelectedHolder = holder
            showToast(context, "You have check out to ${itemGroup.groupName}")

        }

        holder.imgViewBackground.setOnLongClickListener {

            val groupMembersPopup = GroupMembersPopup(context, itemGroup.groupMembers)
            groupMembersPopup.show()


             true

        }


    }


    override fun getItemCount(): Int {
        return arrayListOfGroups.size
    }


    private fun callTheInvitationPopup(groupName: String, groupKey: String, groupItem: Group) {
        val invitationPopup = InvitationPopup(context, groupName, groupKey, groupItem)
        invitationPopup.show()
    }


    private fun callAreYouSureThatYouWantToLeaveTheGroup( groupKey : String) {
        val leaveTheGroupPopup = LeaveTheGroupPopup(context)
        leaveTheGroupPopup.show()

        leaveTheGroupPopup.setOnDismissListener {
            if (leaveTheGroupPopup.hasUserChosenToLeave)
                listener.onUserChoseToLeaveTheGroup(groupKey)
        }
    }

}
