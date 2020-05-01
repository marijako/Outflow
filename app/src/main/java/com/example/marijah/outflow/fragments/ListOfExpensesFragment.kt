package com.example.marijah.outflow.fragments


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marijah.outflow.R
import com.example.marijah.outflow.adapters.ListOfExpensesAdapter
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.ExpenseItem
import com.example.marijah.outflow.popups.DeletePopup
import com.example.marijah.outflow.room_database.Expense
import com.example.marijah.outflow.room_database.ExpenseDatabase
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_list_of_expenses.*


class ListOfExpensesFragment : Fragment(R.layout.fragment_list_of_expenses) {

    private lateinit var childEventListenerForExpenses: ChildEventListener
    private val arrayOfExpenses = arrayListOf<Any>()
    private lateinit var mAdapter: ListOfExpensesAdapter
    private var myReferenceToExpenses: DatabaseReference? = null
    private var expenseDatabase: ExpenseDatabase? = null
    private var isThisSingleMode = AppManager.getInstance(context).hasUserPickedSingleMode

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutAndListeners()
    }

    private fun setLayoutAndListeners() {

        imgViewAddNewBill.setOnClickListener { findNavController().popBackStack() }


        if (!isThisSingleMode) {
            val database = FirebaseDatabase.getInstance()
            myReferenceToExpenses = database.reference.child(AppManager.getInstance(context).currentlyLookedTableName)

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

            expenseDatabase = ExpenseDatabase.getInstance(context)
            for (expense in expenseDatabase!!.expenseDao().expenseList) {
                arrayOfExpenses.add(expense)
            }

        }

        mAdapter = ListOfExpensesAdapter(requireContext(), arrayOfExpenses, object : ListOfExpensesAdapter.ListOfExpensesInterface {
            override fun onUserClickedDeleteItem(expenseItem: Any) {
                //sta radimo ako je korisnik kliknuo brisanjac

                val deletePopup = DeletePopup(requireActivity(), R.layout.popup_delete)
                deletePopup.show()

                deletePopup.setOnDismissListener {
                    // ako je korisnik stisnuo brisanjac
                    if (!isThisSingleMode) {
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
        val layoutManager = LinearLayoutManager(context)
        // postavljamo naopaku listu
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        // Give the recycler view a default layout manager.
        rvListOfExpenses.layoutManager = layoutManager


    }
}
