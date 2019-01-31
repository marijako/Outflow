package com.example.marijah.outflow.activities.activities_single_mode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marijah.outflow.R;
import com.example.marijah.outflow.activities.HomeActivity;
import com.example.marijah.outflow.helpers.HelperManager;

public class SettingsActivity extends Activity implements View.OnClickListener{

    int i = 0;
    ImageView imgViewReceiveNotificationsSwitch, imgViewPasswordSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setLayoutAndListeners();

    }

    private void setLayoutAndListeners()
    {

        TextView txtViewName = findViewById(R.id.txtViewName);
        HelperManager.setTypefaceRegular(getAssets(), txtViewName);

        TextView txtViewReceiveDailyNotifications = findViewById(R.id.txtViewReceiveDailyNotifications);
        txtViewReceiveDailyNotifications.setOnClickListener(this);
        HelperManager.setTypefaceLight(getAssets(), txtViewReceiveDailyNotifications);

        imgViewReceiveNotificationsSwitch = findViewById(R.id.imgViewReceiveNotificationsSwitch);


        TextView txtViewPassword = findViewById(R.id.txtViewPassword);
        txtViewPassword.setOnClickListener(this);
        HelperManager.setTypefaceLight(getAssets(), txtViewPassword);

        imgViewPasswordSwitch = findViewById(R.id.imgViewPasswordSwitch);
        imgViewPasswordSwitch.setOnClickListener(this);

        TextView txtViewSignOut = findViewById(R.id.txtViewSignOut);
        txtViewSignOut.setOnClickListener(this);
        HelperManager.setTypefaceRegular(getAssets(), txtViewSignOut);


        TextView txtViewChangeMode = findViewById(R.id.txtViewChangeMode);
        txtViewChangeMode.setOnClickListener(this);
        HelperManager.setTypefaceRegular(getAssets(), txtViewChangeMode);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.txtViewReceiveDailyNotifications:

                i++;
                if(i%2==0)
                {
                    imgViewReceiveNotificationsSwitch.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));

                }
                else
                    imgViewReceiveNotificationsSwitch.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));



                break;


            case R.id.txtViewPassword:

                Intent intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);

                break;

            case R.id.imgViewPasswordSwitch:

                i++;
                if(i%2==0)
                {
                    imgViewPasswordSwitch.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));

                }
                else
                    imgViewPasswordSwitch.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));

                break;

            case R.id.txtViewSignOut:


                intent = new Intent(this, LoginSingleActivity.class);
                startActivity(intent);


                break;

            case R.id.txtViewChangeMode:

                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);

                break;

        }
    }
}
