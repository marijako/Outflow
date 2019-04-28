package com.example.marijah.outflow.activities.activities_single_mode

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import com.example.marijah.outflow.R
import com.example.marijah.outflow.activities.services.InvitationListenerService
import com.example.marijah.outflow.adapters.CategoryAdapter
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.helpers.categoryPickedObject
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Category
import com.example.marijah.outflow.models.ExpenseItem
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new_entry.*
import java.text.SimpleDateFormat
import java.util.*


class NewEntryActivity : MasterActivity(), DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        showToast(this, "Marija")
    }

    private val RC_SIGN_IN = 123
    private var mUsername: String = ""

    private lateinit var database: FirebaseDatabase
    private lateinit var myReferenceToExpenses: DatabaseReference
    private var categoryList: ArrayList<Category>? = null

    // za autentifikaciju
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener


    private val datePicker: DatePicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("Marija", "OnCreate() NewEntryActivity!")


        setContentView(R.layout.activity_new_entry)


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



        setLayoutAndListeners()

        /**Inicijalizujemo listu kategorija */
        initTheCategoryList()
        /**Kreiramo adapter prosledjujuci niz elemenata */
        val adapter = CategoryAdapter(this, categoryList!!)
        /**Povezujemo adapter sa RecyclerViewom */
        rlListCategory.adapter = adapter
        /**Postavljamo layout manager za nas RecyclerView */
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rlListCategory.layoutManager = layoutManager

        // uspostavljanje veze sa fajrbejs bazom
        database = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()

        /* // kreiramo tabelu ekspenses
         myReferenceToExpenses = database.reference.child("expenses_$mUsername")*/
        //  myRef.setValue("Unos broj $i")

        /*  myReferenceToExpenses.addValueEventListener(object : ValueEventListener {
              override fun onDataChange(dataSnapshot: DataSnapshot) {
                  // This method is called once with the initial value and again
                  // whenever data at this location is updated.
                  //    val value = dataSnapshot.getValue(String::class.java)
                  //   Log.d(TAG, "Value is: $value")
              }

              override fun onCancelled(error: DatabaseError) {
                  // Failed to read value
                  Log.w(TAG, "Failed to read value.", error.toException())
              }
          })*/


    }

    private fun setLayoutAndListeners() {

        imgViewSettings.setOnClickListener {
            intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        imgViewList.setOnClickListener {
            intent = Intent(this, ListOfExpensesActivity::class.java)
            startActivity(intent)
        }


        /** Ako je korisnik kliknuo da doda novi racun*/
        txtViewAddNewExpenseButton.setOnClickListener {

            // trazimo od njega da popuni sva polja
            if (editTextAmount.text.toString().isEmpty() || categoryPickedObject.categoryPicked.isEmpty()) {
                Toast.makeText(this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
            } else {

                myReferenceToExpenses = database.reference.child(AppManager.getInstance(this).currentlyLookedTableName)

                // uzimamo jedinstveni kljuc
                val expenseItemID: String = myReferenceToExpenses.push().key ?: " "

                // pravimo objekat sa svim potrebnim informacijama
                val expense = ExpenseItem(expenseItemID, Integer.parseInt(editTextAmount.text.toString()),
                        categoryPickedObject.categoryPicked,
                        editTextStore.text.toString(),
                        txtViewDate.text.toString(),
                        editTextComment.text.toString())

                // dodajemo taj objekat u bazu
                myReferenceToExpenses.child(expenseItemID).setValue(expense)
                showToast(this, "Item successfully added.")
                cleanTheFields()
            }


        }

        imgViewPie.setOnClickListener {
            intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }

        HelperManager.setTypefaceRegular(assets, txtViewName)


        txtViewDate.text = getTodaysDate()
        txtViewDate.setOnClickListener {

            val newDate = Calendar.getInstance()

            val dpd = DatePickerDialog(this,
                    AlertDialog.THEME_HOLO_LIGHT, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                run {


                    val date: String = if (dayOfMonth < 10) {
                        "0$dayOfMonth"
                    } else {
                        dayOfMonth.toString()
                    }
                    val monthOfTheYear: String = if (month < 10) {
                        "0${month + 1}"
                    } else {
                        { month + 1 }.toString()
                    }
                    val newDate = "$date.$monthOfTheYear.$year."
                    txtViewDate.text = newDate
                }
            }, newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH))


            dpd.show()
        }

    }


    @SuppressLint("SimpleDateFormat")
    private fun getTodaysDate(): String {

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd.MM.yyyy.")
        return df.format(c)
    }


    private fun onSignedInInitialize(displayName: String, email: String) {
        mUsername = displayName

        //firebase database pathsne sme da sadrzi '.', '#', '$', '[', or ']'
        val editedEmail = email.replace('.', '@')

        // kreiramo tabelu ekspenses
        myReferenceToExpenses = database.reference.child("expenses_$editedEmail")

        // postavljamo ime trenutno osluskivane tabele
        AppManager.getInstance(this).currentlyLoggedInUserEmail = editedEmail

        listenToTheInvitations()
        //startService()

    }


    /**
     * Funkcija za pokretanje servisa
     */
    private fun startService() = startService(Intent(baseContext, InvitationListenerService::class.java))

    /**
     * Funkcija za zaustavljanje servisa
     */
    fun stopService() {
        stopService(Intent(baseContext, InvitationListenerService::class.java))
    }


    private fun onSignedOutCleanUp() {

        mUsername = ""
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                showToast(this, "You are now signed in. Welcome to Outflow!")
            } else if (resultCode == RESULT_CANCELED) {
                finish()
                showToast(this, "Sign In Cancelled!")
            }

        }
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
    }

    override fun onPause() {
        super.onPause()
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener)
    }


    private fun cleanTheFields() {
        editTextAmount.text.clear()
        editTextStore.text.clear()
        txtViewDate.text = getTodaysDate()
        editTextComment.text.clear()
    }

    /**
     * Funkcija za inicijalizovanje difoltne liste kategorija
     */
    private fun initTheCategoryList() {

        categoryList = ArrayList()

        val categoryObject = Category()
        categoryObject.categoryName = "Food and Drinks"
        categoryObject.categoryImageName = "food_and_drink"
        categoryList!!.add(categoryObject)

        val categoryObject2 = Category()
        categoryObject2.categoryName = "Bills"
        categoryObject2.categoryImageName = "bills"
        categoryList!!.add(categoryObject2)


        val categoryObject3 = Category()
        categoryObject3.categoryName = "Car And Transport"
        categoryObject3.categoryImageName = "car_and_transport"
        categoryList!!.add(categoryObject3)


        val categoryObject5 = Category()
        categoryObject5.categoryName = "Houseware"
        categoryObject5.categoryImageName = "houseware"
        categoryList!!.add(categoryObject5)


        val categoryObject6 = Category()
        categoryObject6.categoryName = "Trips"
        categoryObject6.categoryImageName = "trips"
        categoryList!!.add(categoryObject6)

        val categoryObject7 = Category()
        categoryObject7.categoryName = "Hygiene"
        categoryObject7.categoryImageName = "hygiene"
        categoryList!!.add(categoryObject7)

        val categoryObject8 = Category()
        categoryObject8.categoryName = "Gifts"
        categoryObject8.categoryImageName = "gifts"
        categoryList!!.add(categoryObject8)


        val categoryObject9 = Category()
        categoryObject9.categoryName = "Clothes"
        categoryObject9.categoryImageName = "clothes"
        categoryList!!.add(categoryObject9)

        val categoryObject10 = Category()
        categoryObject10.categoryName = "Fun"
        categoryObject10.categoryImageName = "fun"
        categoryList!!.add(categoryObject10)

        val categoryObject11 = Category()
        categoryObject11.categoryName = "Other"
        categoryObject11.categoryImageName = "other"
        categoryList!!.add(categoryObject11)


        //  AppManager.getInstance(this).saveArrayList(categoryList, "CATEGORY_LIST");

    }

}
