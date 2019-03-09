package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.HelperManager
import kotlinx.android.synthetic.main.activity_login_single.*

class LoginSingleActivity : Activity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_single)

        setLayoutAndListeners()
    }

    override fun onClick(view: View) {

    }

    private fun setLayoutAndListeners() {


        HelperManager.setTypefaceRegular(assets, txtViewEnterYourPassword)


    }
}
