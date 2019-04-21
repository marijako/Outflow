package com.example.marijah.outflow.activities.activities_single_mode


import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.marijah.outflow.R
import com.example.marijah.outflow.adapters.ListOfExpensesAdapter
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.ExpenseItem
import com.example.marijah.outflow.popups.DeletePopup
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_list_of_expenses.*


class ListOfExpensesActivity : Activity() {

    private lateinit var childEventListenerForExpenses: ChildEventListener
    private val arrayOfExpenses = arrayListOf<ExpenseItem>()
    private lateinit var mAdapter: ListOfExpensesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_expenses)

        setLayoutAndListeners()
    }

    private fun setLayoutAndListeners() {

        imgViewAddNewBill.setOnClickListener { finish() }
        HelperManager.setTypefaceRegular(assets, txtViewName)


        val database = FirebaseDatabase.getInstance()
        val myReferenceToExpenses = database.reference.child(AppManager.getInstance(this).currentlyLookedTableName)

        childEventListenerForExpenses = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {


                val expenseItem = dataSnapshot.getValue(ExpenseItem::class.java)

                // dodajemo u listu troskova
                arrayOfExpenses.add(expenseItem!!)
                mAdapter.notifyDataSetChanged()
                rvListOfExpenses.scrollToPosition(arrayOfExpenses.size - 1)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

                Log.i("Marija", "On Child Changed")
                arrayOfExpenses.clear()
                val expenseItem = dataSnapshot.getValue(ExpenseItem::class.java)

                arrayOfExpenses.add(expenseItem!!)
                mAdapter.notifyDataSetChanged()
                // arrayOfExpenses.set(dataSnapshot.getValue(ExpenseItem::class.java)!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.i("Marija", "On Child Removed")


                for (expense in arrayOfExpenses) {
                    if (expense.key == dataSnapshot.key) {
                        arrayOfExpenses.remove(expense)
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

        myReferenceToExpenses.addChildEventListener(childEventListenerForExpenses)



        mAdapter = ListOfExpensesAdapter(this, arrayOfExpenses, object : ListOfExpensesAdapter.ListOfExpensesInterface {
            override fun onUserClickedDeleteItem(expenseItem: ExpenseItem) {
                //sta radimo ako je korisnik kliknuo brisanjac

                val deletePopup = DeletePopup(this@ListOfExpensesActivity, R.layout.popup_delete)
                deletePopup.show()

                deletePopup.setOnDismissListener {
                    // ako je korisnik stisnuo brisanjac
                    if (deletePopup.hasUserClickedDelete) {
                        myReferenceToExpenses.child(expenseItem.key).removeValue()
                        mAdapter.notifyDataSetChanged()
                    }

                }


            }
        })
        // Connect the mAdapter with the recycler view.
        rvListOfExpenses.adapter = mAdapter
        val layoutManager = LinearLayoutManager(this)
        // postavljamo naopaku listu
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        // Give the recycler view a default layout manager.
        rvListOfExpenses.layoutManager = layoutManager


    }
}
