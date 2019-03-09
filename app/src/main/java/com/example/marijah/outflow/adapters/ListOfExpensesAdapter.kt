package com.example.marijah.outflow.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.helpers.ListOfExpensesOpenHelper
import com.example.marijah.outflow.models.Category
import com.example.marijah.outflow.models.ListOfExpensesItem

import java.util.ArrayList

class ListOfExpensesAdapter// Pass in the contact array into the constructor
(// Easy access to the context object in the recyclerview
        private val context: Context, internal var mDB: ListOfExpensesOpenHelper) : RecyclerView.Adapter<ListOfExpensesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtViewCostList: TextView
        var txtViewCategoryList: TextView
        var txtViewCommentList: TextView
        var imgViewDeleteItem: ImageView

        init {
            txtViewCostList = itemView.findViewById(R.id.txtViewCostList)
            HelperManager.setTypefaceLight(context.assets, txtViewCostList)

            txtViewCategoryList = itemView.findViewById(R.id.txtViewCategoryList)
            HelperManager.setTypefaceLight(context.assets, txtViewCategoryList)

            txtViewCommentList = itemView.findViewById(R.id.txtViewCommentList)
            HelperManager.setTypefaceLight(context.assets, txtViewCommentList)

            imgViewDeleteItem = itemView.findViewById(R.id.imgViewDeleteItem)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfExpensesAdapter.ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_list_of_expenses, parent, false)

        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ListOfExpensesAdapter.ViewHolder, position: Int) {

        val current = mDB.query(position)
        holder.txtViewCostList.text = current.mCost


        val imgViewDeleteItemTemp = holder.imgViewDeleteItem


        imgViewDeleteItemTemp.setOnClickListener {
            // String choosenCategory = categoryElement.getCategoryName();
            Toast.makeText(context, "You have picked " + current.mCost, Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return 10
    }

}
