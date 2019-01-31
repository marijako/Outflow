package com.example.marijah.outflow.activities.activities_single_mode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.marijah.outflow.R;
import com.example.marijah.outflow.helpers.HelperManager;

public class LoginSingleActivity extends Activity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_single);

        setLayoutAndListeners();
    }

    @Override
    public void onClick(View view) {

    }

    private void setLayoutAndListeners() {

        TextView txtViewEnterYourPassword = findViewById(R.id.txtViewEnterYourPassword);
        HelperManager.setTypefaceRegular(getAssets(), txtViewEnterYourPassword);



    }
}
