package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View


import com.example.marijah.outflow.R
import com.example.marijah.outflow.adapters.ListOfExpensesAdapter
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.helpers.ListOfExpensesOpenHelper
import kotlinx.android.synthetic.main.activity_list_of_expenses.*


class ListOfExpensesActivity : Activity() {

    // database
    private var mDB: ListOfExpensesOpenHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_expenses)

        setLayoutAndListeners()

    }

    private fun setLayoutAndListeners() {

        imgViewAddNewBill.setOnClickListener{ finish()}
        HelperManager.setTypefaceRegular(assets, txtViewName)


        mDB = ListOfExpensesOpenHelper(this)

        val mAdapter = ListOfExpensesAdapter(this, mDB!!)
        // Create recycler view.
        val mRecyclerView = findViewById<View>(R.id.rvListOfExpenses) as RecyclerView
        // Connect the mAdapter with the recycler view.
        mRecyclerView.adapter = mAdapter
        // Give the recycler view a default layout manager.
        mRecyclerView.layoutManager = LinearLayoutManager(this)


    }
}
