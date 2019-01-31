package com.example.marijah.outflow.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marijah.outflow.R;
import com.example.marijah.outflow.activities.activities_single_mode.ChangePasswordActivity;
import com.example.marijah.outflow.activities.activities_single_mode.NewEntryActivity;
import com.example.marijah.outflow.helpers.HelperManager;

public class HomeActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setLayoutAndListeners();


    }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.imgViewTrackYourExpensesButton:

                Intent intent = new Intent(this, NewEntryActivity.class);
                startActivity(intent);

                break;

            case R.id.imgViewTrackGroupExpensesButton:




                break;

            case R.id.imgViewInvitedButton:

                intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);

                break;
        }
    }


    // setting up the listeners on our activity
    private void setLayoutAndListeners()
    {

        ImageView imgViewTrackYourExpensesButton = findViewById(R.id.imgViewTrackYourExpensesButton);
        imgViewTrackYourExpensesButton.setOnClickListener(this);

        ImageView imgViewTrackGroupExpensesButton = findViewById(R.id.imgViewTrackGroupExpensesButton);
        imgViewTrackGroupExpensesButton.setOnClickListener(this);

        ImageView imgViewInvitedButton = findViewById(R.id.imgViewInvitedButton);
        imgViewInvitedButton.setOnClickListener(this);


        TextView txtViewTrackYourExpensesButton = findViewById(R.id.txtViewTrackYourExpensesButton);
        HelperManager.setTypefaceRegular(getAssets(), txtViewTrackYourExpensesButton);

        TextView txtViewTrackGroupExpensesButton = findViewById(R.id.txtViewTrackGroupExpensesButton);
        HelperManager.setTypefaceRegular(getAssets(), txtViewTrackGroupExpensesButton);

        TextView txtViewInvitedButton = findViewById(R.id.txtViewInvitedButton);
        HelperManager.setTypefaceRegular(getAssets(), txtViewInvitedButton);


    }

}
