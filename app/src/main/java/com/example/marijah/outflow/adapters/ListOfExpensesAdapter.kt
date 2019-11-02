package com.example.marijah.outflow.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.marijah.outflow.R
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.ExpenseItem
import com.example.marijah.outflow.room_database.Expense
import java.util.*

class ListOfExpensesAdapter(private val context: Context, private val arrayListOfExpenses: ArrayList<Any>, private val listener: ListOfExpensesInterface) : RecyclerView.Adapter<ListOfExpensesAdapter.ViewHolder>() {


    interface ListOfExpensesInterface {
        fun onUserClickedDeleteItem(expenseItem: Any)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtViewCostList: TextView = itemView.findViewById(R.id.txtViewCostList)
        var txtViewStoreList : TextView = itemView.findViewById(R.id.txtViewStoreList)
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


        if(!AppManager.getInstance(context).hasUserPickedSingleMode) {
            val expenseItem = arrayListOfExpenses[position] as ExpenseItem

            holder.txtViewCostList.text = expenseItem.price.toString()
            holder.txtViewStoreList.text = expenseItem.place.toUpperCase()
            holder.txtViewCategoryList.text = expenseItem.category
            holder.txtViewCommentList.text = expenseItem.comment
            holder.txtViewDateList.text = expenseItem.date

            holder.imgViewDeleteItem.setOnClickListener {
                listener.onUserClickedDeleteItem(expenseItem)
            }
        }
        else {
            val expenseItem = arrayListOfExpenses[position] as Expense

            holder.txtViewCostList.text = expenseItem.price.toString()
            holder.txtViewStoreList.text = expenseItem.place.toUpperCase()
            holder.txtViewCategoryList.text = expenseItem.category
            holder.txtViewCommentList.text = expenseItem.comment
            holder.txtViewDateList.text = expenseItem.date

            holder.imgViewDeleteItem.setOnClickListener {
                listener.onUserClickedDeleteItem(expenseItem)
            }
        }
    }


    override fun getItemCount(): Int {
        return arrayListOfExpenses.size
    }

}
