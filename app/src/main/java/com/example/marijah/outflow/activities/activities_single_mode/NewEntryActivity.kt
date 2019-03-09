package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.marijah.outflow.R
import com.example.marijah.outflow.adapters.CategoryAdapter
import com.example.marijah.outflow.helpers.HelperManager
import com.example.marijah.outflow.models.Category
import kotlinx.android.synthetic.main.activity_new_entry.*
import java.util.*

class NewEntryActivity : Activity() {


    private var categoryList: ArrayList<Category>? = null


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

        txtViewAddBillButton.setOnClickListener {}

        HelperManager.setTypefaceRegular(assets, editTextAmount)
        HelperManager.setTypefaceRegular(assets, editTextCategory)
        HelperManager.setTypefaceRegular(assets, editTextStore)
        HelperManager.setTypefaceRegular(assets, editTextDate)
        HelperManager.setTypefaceRegular(assets, txtViewName)
        HelperManager.setTypefaceRegular(assets, txtViewAddBillButton!!)

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
