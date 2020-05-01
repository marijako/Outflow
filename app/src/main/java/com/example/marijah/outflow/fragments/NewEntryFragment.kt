package com.example.marijah.outflow.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marijah.outflow.R
import com.example.marijah.outflow.adapters.CategoryAdapter
import com.example.marijah.outflow.helpers.categoryPickedObject
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.ExpenseItem
import com.example.marijah.outflow.room_database.Expense
import com.example.marijah.outflow.room_database.ExpenseDatabase
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new_entry.*
import java.text.SimpleDateFormat
import java.util.*


class NewEntryFragment : Fragment(R.layout.fragment_new_entry_single) {

    // view model vazi za sve
    private lateinit var viewModel: NewEntryViewModel

    // za lokalno
    private var expenseDatabase: ExpenseDatabase? = null

    // za grupno
    private val RC_SIGN_IN = 123
    private var mUsername: String = ""

    // za pristup bazi
    private var database: FirebaseDatabase? = null
    private var myReferenceToExpenses: DatabaseReference? = null

    // za autentifikaciju
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[NewEntryViewModel::class.java]

        if (AppManager.getInstance(context).hasUserPickedSingleMode)
            expenseDatabase = ExpenseDatabase.getInstance(context)
        else {
            mAuthStateListener = FirebaseAuth.AuthStateListener {

                val user = it.currentUser
                if (user != null) {
                    // korisnik je prijavljen
                    onSignedInInitialize(user.displayName!!, user.email!!)
                } else {
                    // korisnik je odjavljen

                    onSignedOutCleanUp()


                    val customLayout = AuthMethodPickerLayout.Builder(R.layout.activity_firebase_sign_in)
                            .setGoogleButtonId(R.id.txtViewGmailSignIn)
                            .setEmailButtonId(R.id.txtViewEmailSignIn)
                            .build()


                    startActivityForResult(

                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setTheme(R.style.FirebaseAuthTheme)
                                    .setAuthMethodPickerLayout(customLayout)
                                    .setAvailableProviders(Arrays.asList(
                                            AuthUI.IdpConfig.GoogleBuilder().build(),
                                            AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN)


                }
            }

            // uspostavljanje veze sa fajrbejs bazom
            database = FirebaseDatabase.getInstance()
            mFirebaseAuth = FirebaseAuth.getInstance()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            setLayoutAndListeners()
            /**Kreiramo adapter prosledjujuci niz elemenata */
            val adapter = CategoryAdapter(it, viewModel.categoryList)
            /**Povezujemo adapter sa RecyclerViewom */
            rlListCategory.adapter = adapter
            /**Postavljamo layout manager za nas RecyclerView */
            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
            rlListCategory.layoutManager = layoutManager
        }
    }


    private fun setLayoutAndListeners() {

        imgViewSettings.setOnClickListener {
            findNavController().navigate(R.id.action_newEntrySingleFragment_to_settingsActivity)
        }

        imgViewList.setOnClickListener {
            findNavController().navigate(R.id.action_newEntrySingleFragment_to_listOfExpensesFragment)
        }


        /** Ako je korisnik kliknuo da doda novi racun*/
        txtViewAddNewExpenseButton.setOnClickListener {


            // trazimo od njega da popuni sva polja
            if (editTextAmount.text.toString().isEmpty() || categoryPickedObject.categoryPicked.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
            } else {

                if (AppManager.getInstance(context).hasUserPickedSingleMode) {

                    expenseDatabase?.expenseDao()?.insertExpense(Expense(Integer.parseInt(editTextAmount.text.toString()),
                            categoryPickedObject.categoryPicked,
                            editTextStore.text.toString(),
                            txtViewDate.text.toString(),
                            editTextComment.text.toString()))


                    // dodajemo taj objekat u bazu


                } else {
                    myReferenceToExpenses = database?.reference?.child(AppManager.getInstance(context).currentlyLookedTableName)

                    myReferenceToExpenses?.let {
                        // uzimamo jedinstveni kljuc
                        val expenseItemID: String = it.push().key ?: " "

                        // pravimo objekat sa svim potrebnim informacijama
                        val expense = ExpenseItem(expenseItemID, Integer.parseInt(editTextAmount.text.toString()),
                                categoryPickedObject.categoryPicked,
                                editTextStore.text.toString(),
                                txtViewDate.text.toString(),
                                editTextComment.text.toString(),
                                AppManager.getInstance(context).currentlyLoggedInUserEmail)

                        // dodajemo taj objekat u bazu
                        it.child(expenseItemID).setValue(expense)

                    }
                }

                context?.let {
                    showToast(it, "Item successfully added.")
                }
                cleanTheFields()
            }


        }

        imgViewPie.setOnClickListener {
            findNavController().navigate(R.id.action_newEntrySingleFragment_to_statsSingleFragment)
        }

        //HelperManager.setTypefaceRegular(requireContext()!!.assets, txtViewName)


        txtViewDate.text = getTodaysDate()
        txtViewDate.setOnClickListener {
            addDateToLocalDatabase()
        }

    }

    /**
     * Funkcija za dodavanje item-a u lokalnu bazu podataka.
     */
    private fun addDateToLocalDatabase() {
        context?.let {
            val newDate = Calendar.getInstance()

            val dpd = DatePickerDialog(it, AlertDialog.THEME_HOLO_LIGHT, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                run {

                    val date: String = if (dayOfMonth < 10) {
                        "0$dayOfMonth"
                    } else {
                        dayOfMonth.toString()
                    }

                    val monthOfTheYear: String = if (month < 9) {
                        "0${month + 1}"
                    } else {
                        "${month + 1}"
                    }
                    val newDate = "$date.$monthOfTheYear.$year."
                    txtViewDate.text = newDate

                }
            }, newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH))


            dpd.show()
        }
    }


    private fun onSignedInInitialize(displayName: String, email: String) {
        mUsername = displayName

        //firebase database pathsne sme da sadrzi '.', '#', '$', '[', or ']'
        val editedEmail = email.replace('.', '@')

        // kreiramo tabelu ekspenses
        myReferenceToExpenses = database?.reference?.child("expenses_$editedEmail")

        // postavljamo ime trenutno osluskivane tabele
        AppManager.getInstance(context).currentlyLoggedInUserEmail = editedEmail

        //listenToTheInvitations()
        //startService()

    }


    private fun onSignedOutCleanUp() {

        mUsername = ""
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                context?.let {
                    showToast(it, "You are now signed in. Welcome to Outflow!")
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                findNavController().popBackStack()
                context?.let {
                    showToast(it, "Sign In Cancelled!")
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (!AppManager.getInstance(context).hasUserPickedSingleMode)
            mAuthStateListener?.let {
                mFirebaseAuth?.addAuthStateListener(it)
            }
    }

    override fun onPause() {
        super.onPause()
        if (!AppManager.getInstance(context).hasUserPickedSingleMode)
            mAuthStateListener?.let {
                mFirebaseAuth?.removeAuthStateListener(it)
            }
    }


    @SuppressLint("SimpleDateFormat")
    private fun getTodaysDate(): String {

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd.MM.yyyy.")
        return df.format(c)
    }


    private fun cleanTheFields() {
        editTextAmount.text.clear()
        editTextStore.text.clear()
        txtViewDate.text = getTodaysDate()
        editTextComment.text.clear()
    }


}
