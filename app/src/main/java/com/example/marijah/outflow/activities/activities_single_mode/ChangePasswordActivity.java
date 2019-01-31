package com.example.marijah.outflow.activities.activities_single_mode;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marijah.outflow.R;
import com.example.marijah.outflow.helpers.HelperManager;

public class ChangePasswordActivity extends Activity implements View.OnClickListener{


    private TextView editTextOldPassword, editTextNewPassword1, editTextNewPassword2;
    private ImageView imgViewOldPasswordCheck, imgViewNewPasswordCheck1,imgViewNewPasswordCheck2;
    private boolean hasUserGoiItAll = false;
    String s= "Marija";
    private String newPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setLayoutAndListeners();

    }


    private void setLayoutAndListeners()
    {
        TextView txtViewName = findViewById(R.id.txtViewName);
        HelperManager.setTypefaceRegular(getAssets(), txtViewName);

        TextView txtViewConfirmButton = findViewById(R.id.txtViewConfirmButton);
        txtViewConfirmButton.setOnClickListener(this);
        HelperManager.setTypefaceRegular(getAssets(), txtViewConfirmButton);


        imgViewOldPasswordCheck = findViewById(R.id.imgViewOldPasswordCheck);
        imgViewNewPasswordCheck1 = findViewById(R.id.imgViewNewPasswordCheck1);
        imgViewNewPasswordCheck2 = findViewById(R.id.imgViewNewPasswordCheck2);

        editTextOldPassword = findViewById(R.id.editTextOldPassword);
        HelperManager.setTypefaceRegular(getAssets(), editTextOldPassword);

        editTextOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if(charSequence.toString().equalsIgnoreCase(s)) {
                    imgViewOldPasswordCheck.setVisibility(View.VISIBLE);
                    editTextNewPassword1.setFocusableInTouchMode(true);
                }
                    else {
                    imgViewOldPasswordCheck.setVisibility(View.INVISIBLE);
                    editTextNewPassword1.setFocusable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        editTextNewPassword1 = findViewById(R.id.editTextNewPassword1);
        editTextNewPassword1.setFocusable(false);
        HelperManager.setTypefaceRegular(getAssets(), editTextNewPassword1);

        editTextNewPassword1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if(charSequence.toString().length()>5) {
                    imgViewNewPasswordCheck1.setVisibility(View.VISIBLE);
                    editTextNewPassword2.setFocusableInTouchMode(true);
                    newPassword = charSequence.toString();
                }
                else {
                    imgViewNewPasswordCheck1.setVisibility(View.INVISIBLE);
                    editTextNewPassword2.setFocusable(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        editTextNewPassword2 = findViewById(R.id.editTextNewPassword2);
        editTextNewPassword2.setFocusable(false);
        HelperManager.setTypefaceRegular(getAssets(), editTextNewPassword2);

        editTextNewPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if(charSequence.toString().equalsIgnoreCase(newPassword)) {
                    imgViewNewPasswordCheck2.setVisibility(View.VISIBLE);
                    hasUserGoiItAll = true;
                }
                else {
                    imgViewNewPasswordCheck2.setVisibility(View.INVISIBLE);
                    hasUserGoiItAll = false;
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.txtViewConfirmButton:

                if(hasUserGoiItAll)
                {
                    Toast.makeText(this, "Password successfully changed!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Please fill in all the fields above", Toast.LENGTH_SHORT).show();
                }

                break;


        }

    }
}
