package com.example.marijah.outflow.activities.activities_single_mode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marijah.outflow.adapters.CategoryAdapter;
import com.example.marijah.outflow.adapters.ListOfExpensesAdapter;
import com.example.marijah.outflow.helpers.ListOfExpensesOpenHelper;
import com.example.marijah.outflow.models.AppManager;
import com.example.marijah.outflow.models.Category;
import com.example.marijah.outflow.R;
import com.example.marijah.outflow.helpers.HelperManager;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.util.ArrayList;

public class NewEntryActivity extends Activity implements View.OnClickListener {

    private TextView editTextAmount, editTextCategory, editTextStore, editTextDate;
    private ImageView imgViewSettings, imgViewList;
    private TextView txtViewAddBillButton;

    private ArrayList<Category> categoryList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        setLayoutAndListeners();


        /**Uzimamo listu kategorija*/
      //  categoryList = AppManager.getInstance(this).getArrayList("CATEGORY_LIST");
     //   if (categoryList == null)
            initTheCategoryList();

        /**Nadjemo RecyclerView*/
        RecyclerView rvCategories = (RecyclerView) findViewById(R.id.rlListCategory);

        /**Kreiramo adapter prosledjujuci niz elemenata*/
        CategoryAdapter adapter = new CategoryAdapter(this, categoryList);

        /**Povezujemo adapter sa RecyclerViewom*/
        rvCategories.setAdapter(adapter);

        /**Postavljamo layout manager za nas RecyclerView*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvCategories.setLayoutManager(layoutManager);

       // rvCategories.setLayoutManager(new LinearLayoutManager(getApplicationContext()));




    }

    /**
     * Funkcija za inicijalizovanje difoltne liste kategorija
     */
    private void initTheCategoryList() {

        categoryList = new ArrayList<>();

        Category categoryObject = new Category();
        categoryObject.setCategoryName("Food and Drinks");
        categoryObject.setCategoryImageName("food_and_drink");
        categoryList.add(categoryObject);

        Category categoryObject2 = new Category();
        categoryObject2.setCategoryName("Bills");
        categoryObject2.setCategoryImageName("bills");
        categoryList.add(categoryObject2);


        Category categoryObject3 = new Category();
        categoryObject3.setCategoryName("Car And Transport");
        categoryObject3.setCategoryImageName("car_and_transport");
        categoryList.add(categoryObject3);


        Category categoryObject5 = new Category();
        categoryObject5.setCategoryName("Houseware");
        categoryObject5.setCategoryImageName("houseware");
        categoryList.add(categoryObject5);


        Category categoryObject6 = new Category();
        categoryObject6.setCategoryName("Trips");
        categoryObject6.setCategoryImageName("trips");
        categoryList.add(categoryObject6);

        Category categoryObject7 = new Category();
        categoryObject7.setCategoryName("Hygiene");
        categoryObject7.setCategoryImageName("hygiene");
        categoryList.add(categoryObject7);

        Category categoryObject8 = new Category();
        categoryObject8.setCategoryName("Gifts");
        categoryObject8.setCategoryImageName("gifts");
        categoryList.add(categoryObject8);


        Category categoryObject9 = new Category();
        categoryObject9.setCategoryName("Clothes");
        categoryObject9.setCategoryImageName("clothes");
        categoryList.add(categoryObject9);

        Category categoryObject10 = new Category();
        categoryObject10.setCategoryName("Fun");
        categoryObject10.setCategoryImageName("fun");
        categoryList.add(categoryObject10);

        Category categoryObject11 = new Category();
        categoryObject11.setCategoryName("Other");
        categoryObject11.setCategoryImageName("other");
        categoryList.add(categoryObject11);


      //  AppManager.getInstance(this).saveArrayList(categoryList, "CATEGORY_LIST");

    }


    private void setLayoutAndListeners() {

        editTextAmount = findViewById(R.id.editTextAmount);
        HelperManager.setTypefaceRegular(getAssets(), editTextAmount);

        editTextCategory = findViewById(R.id.editTextCategory);
        HelperManager.setTypefaceRegular(getAssets(), editTextCategory);

        editTextStore = findViewById(R.id.editTextStore);
        HelperManager.setTypefaceRegular(getAssets(), editTextStore);

        editTextDate = findViewById(R.id.editTextDate);
        HelperManager.setTypefaceRegular(getAssets(), editTextDate);

        imgViewSettings = findViewById(R.id.imgViewSettings);
        imgViewSettings.setOnClickListener(this);

        imgViewList = findViewById(R.id.imgViewList);
        imgViewList.setOnClickListener(this);

        imgViewSettings = findViewById(R.id.imgViewSettings);
        imgViewSettings.setOnClickListener(this);

        TextView txtViewName = findViewById(R.id.txtViewName);
        HelperManager.setTypefaceRegular(getAssets(), txtViewName);

        txtViewAddBillButton = findViewById(R.id.txtViewAddBillButton);
        txtViewAddBillButton.setOnClickListener(this);
        HelperManager.setTypefaceRegular(getAssets(), txtViewAddBillButton);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imgViewSettings:

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                break;

            case R.id.imgViewList:

                intent = new Intent(this, ListOfExpensesActivity.class);
                startActivity(intent);

                break;

        }

    }
}
