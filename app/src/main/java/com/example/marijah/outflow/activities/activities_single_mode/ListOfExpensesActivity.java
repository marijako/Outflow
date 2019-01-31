package com.example.marijah.outflow.activities.activities_single_mode;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marijah.outflow.R;
import com.example.marijah.outflow.adapters.ListOfExpensesAdapter;
import com.example.marijah.outflow.helpers.HelperManager;
import com.example.marijah.outflow.helpers.ListOfExpensesOpenHelper;

public class ListOfExpensesActivity extends Activity implements View.OnClickListener {

    // database
    private ListOfExpensesOpenHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_expenses);

        setLayoutAndListeners();

    }

    private void setLayoutAndListeners()
    {

        TextView txtViewName = findViewById(R.id.txtViewName);
        HelperManager.setTypefaceRegular(getAssets(), txtViewName);

        ImageView imgViewAddNewBill = findViewById(R.id.imgViewAddNewBill);
        imgViewAddNewBill.setOnClickListener(this);


        mDB = new ListOfExpensesOpenHelper(this);

        ListOfExpensesAdapter mAdapter = new ListOfExpensesAdapter(this, mDB);

        // Create recycler view.
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rvListOfExpenses);

        // Connect the mAdapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.imgViewAddNewBill:

                finish();

                break;

        }

    }
}
