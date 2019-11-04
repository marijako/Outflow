package com.example.marijah.outflow.activities.activities_group_mode


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
import com.example.marijah.outflow.room_database.Expense
import com.example.marijah.outflow.room_database.ExpenseDatabase
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_list_of_expenses.*


class ListOfExpensesActivity : Activity() {

    private lateinit var childEventListenerForExpenses: ChildEventListener
    private val arrayOfExpenses = arrayListOf<Any>()
    private lateinit var mAdapter: ListOfExpensesAdapter
    private var myReferenceToExpenses: DatabaseReference? = null
    private var expenseDatabase: ExpenseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_expenses)

        setLayoutAndListeners()
    }

    private fun setLayoutAndListeners() {

        imgViewAddNewBill.setOnClickListener { finish() }
        HelperManager.setTypefaceRegular(assets, txtViewName)


        if (!AppManager.getInstance(this).hasUserPickedSingleMode) {
            val database = FirebaseDatabase.getInstance()
            myReferenceToExpenses = database.reference.child(AppManager.getInstance(this).currentlyLookedTableName)

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
                        if ((expense as ExpenseItem).key == dataSnapshot.key) {
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

            myReferenceToExpenses!!.addChildEventListener(childEventListenerForExpenses)
        } else {
            // iz ROOMA

            expenseDatabase = ExpenseDatabase.getInstance(this)
            for (expense in expenseDatabase!!.expenseDao().expenseList) {
                arrayOfExpenses.add(expense)
            }

        }

        mAdapter = ListOfExpensesAdapter(this, arrayOfExpenses, object : ListOfExpensesAdapter.ListOfExpensesInterface {
            override fun onUserClickedDeleteItem(expenseItem: Any) {
                //sta radimo ako je korisnik kliknuo brisanjac

                val deletePopup = DeletePopup(this@ListOfExpensesActivity, R.layout.popup_delete)
                deletePopup.show()

                deletePopup.setOnDismissListener {
                    // ako je korisnik stisnuo brisanjac
                    if (!AppManager.getInstance(applicationContext).hasUserPickedSingleMode) {
                        if (deletePopup.hasUserClickedDelete) {
                            myReferenceToExpenses!!.child((expenseItem as ExpenseItem).key).removeValue()
                           // arrayOfExpenses.remove(expenseItem as ExpenseItem)
                            mAdapter.notifyDataSetChanged()
                        }

                    } else {
                        if (deletePopup.hasUserClickedDelete) {
                            expenseDatabase?.expenseDao()?.deleteExpense(expenseItem as Expense)
                            arrayOfExpenses.remove(expenseItem as Expense)
                            mAdapter.notifyDataSetChanged()
                        }
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
