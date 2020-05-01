package com.example.marijah.outflow.fragments

import androidx.lifecycle.ViewModel
import com.example.marijah.outflow.models.Category
import java.util.*
import kotlin.collections.ArrayList

class NewEntryViewModel : ViewModel(){

    val categoryList : ArrayList<Category> = arrayListOf()

    init {
        /**Inicijalizujemo listu kategorija */
        initTheCategoryList()
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

    override fun onCleared() {
        super.onCleared()
    }
}