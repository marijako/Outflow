package com.example.marijah.outflow.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.marijah.outflow.R
import com.example.marijah.outflow.models.ExpenseItem
import java.util.*

class ListOfExpensesAdapter(private val context: Context, private val arrayListOfExpenses: ArrayList<ExpenseItem>, private val listener: ListOfExpensesInterface) : RecyclerView.Adapter<ListOfExpensesAdapter.ViewHolder>() {


    interface ListOfExpensesInterface {
        fun onUserClickedDeleteItem(expenseItem: ExpenseItem)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtViewCostList: TextView = itemView.findViewById(R.id.txtViewCostList)
        var txtViewCategoryList: TextView = itemView.findViewById(R.id.txtViewCategoryList)
        var txtViewCommentList: TextView = itemView.findViewById(R.id.txtViewCommentList)
        var txtViewDateList: TextView = itemView.findViewById(R.id.txtViewDateList)
        var imgViewDeleteItem: ImageView = itemView.findViewById(R.id.imgViewDeleteItem)

        init {

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

        val expenseItem = arrayListOfExpenses[position]
        holder.txtViewCostList.text = expenseItem.price.toString()
        holder.txtViewCategoryList.text = expenseItem.category
        holder.txtViewCommentList.text = expenseItem.comment
        holder.txtViewDateList.text = expenseItem.date


        holder.imgViewDeleteItem.setOnClickListener {
            listener.onUserClickedDeleteItem(expenseItem)
        }

    }


    override fun getItemCount(): Int {
        return arrayListOfExpenses.size
    }

}