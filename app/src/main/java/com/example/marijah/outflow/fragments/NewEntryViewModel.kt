package com.example.marijah.outflow.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.models.Category
import com.example.marijah.outflow.models.ExpenseItem
import com.example.marijah.outflow.room_database.Expense
import com.example.marijah.outflow.room_database.ExpenseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewEntryViewModel(application: Application) : AndroidViewModel(application) {

    // lokalna baza podataka
    var expenseDatabase: ExpenseDatabase? = null
        private set

    // za pristup Firebase bazi
    private var database: FirebaseDatabase? = null
    var myReferenceToExpenses: DatabaseReference? = null
        private set

    // za autentifikaciju
    var mFirebaseAuth: FirebaseAuth? = null
        private set
    var mAuthStateListener: FirebaseAuth.AuthStateListener? = null
        private set

    // request kod za prijavljivanje
    val RC_SIGN_IN = 123

    var userShouldSignUp = MutableLiveData<Boolean>()
        private set

    // lista kategorija
    val categoryList: ArrayList<Category> = arrayListOf()

    var testDataBinding = MutableLiveData("Hello")

    init {
        /**Inicijalizujemo listu kategorija */
        initTheCategoryList()

        if (AppManager.getInstance(application).hasUserPickedSingleMode)
            expenseDatabase = ExpenseDatabase.getInstance(application)
        else {
            mAuthStateListener = FirebaseAuth.AuthStateListener {

                val user = it.currentUser
                if (user != null) {
                    // korisnik je prijavljen
                    //onSignedInInitialize(user.displayName!!, user.email!!)
                    userShouldSignUp.value = false

                    //firebase database pathsne sme da sadrzi '.', '#', '$', '[', or ']'
                    val editedEmail = user.email?.replace('.', '@')

                    // kreiramo tabelu ekspenses
                    myReferenceToExpenses = database?.reference?.child("expenses_$editedEmail")

                    // postavljamo ime trenutno osluskivane tabele
                    AppManager.getInstance(application).currentlyLoggedInUserEmail = editedEmail

                } else {
                    // korisnik je odjavljen

                    //onSignedOutCleanUp()
                    userShouldSignUp.value = true
                }
            }
            // uspostavljanje veze sa fajrbejs bazom
            database = FirebaseDatabase.getInstance()
            mFirebaseAuth = FirebaseAuth.getInstance()
        }
    }

    /**
     * Funkcija za inicijalizovanje difoltne liste kategorija
     */
    private fun initTheCategoryList() {

        categoryList.add(Category("Food and Drinks", "food_and_drink"))
        categoryList.add(Category("Bills", "bills"))
        categoryList.add(Category("Car And Transport", "car_and_transport"))
        categoryList.add(Category("Houseware", "houseware"))
        categoryList.add(Category("Trips", "trips"))
        categoryList.add(Category("Hygiene", "hygiene"))
        categoryList.add(Category("Gifts", "gifts"))
        categoryList.add(Category("Clothes", "clothes"))
        categoryList.add(Category("Fun", "fun"))
        categoryList.add(Category("Other", "other"))
    }


    /**
     * Funkcija za ubacivanje novog troska u lokalnu bazu koristeci Room.
     */
    fun insertExpenseInRoomDatabase(expense: Expense) {
        expenseDatabase?.expenseDao()?.insertExpense(expense)
    }

    /**
     * Funkcija za ubacivanje novog troska u Firebase Realtime Database.
     */
    fun insertExpenseInFirebaseRealtimeDatabase(expenseItem: ExpenseItem) {
        // ako je u grupnom režimu, dodajemo tošak u odgovarajuću tabelu grupe čiji je korisnik trenutno član
        myReferenceToExpenses = database?.reference?.child(AppManager.getInstance(getApplication()).currentlyLookedTableName)

        myReferenceToExpenses?.let {
            // dodajemo taj objekat u bazu pod jedinstvenim kljucem
            it.child(expenseItem.key).setValue(expenseItem)
        }
    }


    override fun onCleared() {
        super.onCleared()
    }
}