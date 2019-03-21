package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.marijah.outflow.R
import com.example.marijah.outflow.adapters.CategoryAdapter
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.helpers.TAG
import com.example.marijah.outflow.helpers.categoryPickedObject
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.Category
import com.example.marijah.outflow.models.ExpenseItem
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_new_entry.*
import java.util.*
import com.firebase.ui.auth.AuthMethodPickerLayout




class NewEntryActivity : Activity() {
    private val RC_SIGN_IN = 123

    private var mUsername : String = ""
    private lateinit var myReferenceToExpenses: DatabaseReference
    private var categoryList: ArrayList<Category>? = null

    // za autentifikaciju
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)

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
        val database = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()


        myReferenceToExpenses = database.reference.child("expenses")
        //  myRef.setValue("Unos broj $i")

        myReferenceToExpenses.addValueEventListener(object : ValueEventListener {
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
        })

        mAuthStateListener = FirebaseAuth.AuthStateListener {

            val user = it.currentUser
            if (user != null) {
                // korisnik je prijavljen
                onSignedInInitialize(user.displayName!!)
            } else {
                // korisnik je odjavljen

                onSignedOutCleanUp()


                val customLayout = AuthMethodPickerLayout.Builder(R.layout.activity_firebase_sign_in)
                        .setGoogleButtonId(R.id.imgViewGmailSignIn)
                        .setEmailButtonId(R.id.imgViewEmailSignIn)
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


    }

    private fun onSignedInInitialize(displayName: String) {
        mUsername = displayName
    }

    private fun onSignedOutCleanUp() {

        mUsername = ""
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN)
        {
            if(requestCode == RESULT_OK)
            {
                showToast(this, "You are now signed in. Welcome to Outflow!")
            }
            else if(requestCode == RESULT_CANCELED)
            {
                showToast(this, "Sign In Cancelled!")
                finish()
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
        txtViewAddBillButton.setOnClickListener {

            if (editTextAmount.text.toString().isEmpty() || categoryPickedObject.categoryPicked.isEmpty()) {
                Toast.makeText(this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
            } else {
                val expense = ExpenseItem(Integer.parseInt(editTextAmount.text.toString()),
                        categoryPickedObject.categoryPicked,
                        editTextStore.text.toString(),
                        editTextDate.text.toString(),
                        editTextComment.text.toString())

                myReferenceToExpenses.push().setValue(expense)

                showToast(this, "Item successfully added.")

                cleanTheFields()
            }


        }

        imgViewPie.setOnClickListener {
            intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }


        HelperManager.setTypefaceRegular(assets, editTextAmount)
        HelperManager.setTypefaceRegular(assets, editTextCategory)
        HelperManager.setTypefaceRegular(assets, editTextStore)
        HelperManager.setTypefaceRegular(assets, editTextDate)
        HelperManager.setTypefaceRegular(assets, txtViewName)
        HelperManager.setTypefaceRegular(assets, txtViewAddBillButton!!)
        HelperManager.setTypefaceRegular(assets, editTextComment)
    }


    private fun cleanTheFields() {
        editTextAmount.text.clear()
        editTextStore.text.clear()
        editTextDate.text.clear()
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
