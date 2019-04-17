package com.example.marijah.outflow.activities.activities_single_mode

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.HelperManager

class ChangePasswordActivity : Activity(), View.OnClickListener {


    private var editTextOldPassword: TextView? = null
    private var editTextNewPassword1: TextView? = null
    private var editTextNewPassword2: TextView? = null
    private var imgViewOldPasswordCheck: ImageView? = null
    private var imgViewNewPasswordCheck1: ImageView? = null
    private var imgViewNewPasswordCheck2: ImageView? = null
    private var hasUserGoiItAll = false
    internal var s = "Marija"
    private var newPassword: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        setLayoutAndListeners()

    }


    private fun setLayoutAndListeners() {
        val txtViewName = findViewById<TextView>(R.id.txtViewName)
        HelperManager.setTypefaceRegular(assets, txtViewName)

        val txtViewConfirmButton = findViewById<TextView>(R.id.txtViewConfirmButton)
        txtViewConfirmButton.setOnClickListener(this)
        HelperManager.setTypefaceRegular(assets, txtViewConfirmButton)


        imgViewOldPasswordCheck = findViewById(R.id.imgViewOldPasswordCheck)
        imgViewNewPasswordCheck1 = findViewById(R.id.imgViewNewPasswordCheck1)
        imgViewNewPasswordCheck2 = findViewById(R.id.imgViewNewPasswordCheck2)

        editTextOldPassword = findViewById(R.id.editTextOldPassword)

        editTextOldPassword!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {


                if (charSequence.toString().equals(s, ignoreCase = true)) {
                    imgViewOldPasswordCheck!!.visibility = View.VISIBLE
                    editTextNewPassword1!!.isFocusableInTouchMode = true
                } else {
                    imgViewOldPasswordCheck!!.visibility = View.INVISIBLE
                    editTextNewPassword1!!.isFocusable = false
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })


        editTextNewPassword1 = findViewById(R.id.editTextNewPassword1)
        editTextNewPassword1!!.isFocusable = false

        editTextNewPassword1!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {


            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {


                if (charSequence.toString().length > 5) {
                    imgViewNewPasswordCheck1!!.visibility = View.VISIBLE
                    editTextNewPassword2!!.isFocusableInTouchMode = true
                    newPassword = charSequence.toString()
                } else {
                    imgViewNewPasswordCheck1!!.visibility = View.INVISIBLE
                    editTextNewPassword2!!.isFocusable = false
                }

            }

            override fun afterTextChanged(editable: Editable) {

            }
        })



        editTextNewPassword2 = findViewById(R.id.editTextNewPassword2)
        editTextNewPassword2!!.isFocusable = false

        editTextNewPassword2!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {


                if (charSequence.toString().equals(newPassword!!, ignoreCase = true)) {
                    imgViewNewPasswordCheck2!!.visibility = View.VISIBLE
                    hasUserGoiItAll = true
                } else {
                    imgViewNewPasswordCheck2!!.visibility = View.INVISIBLE
                    hasUserGoiItAll = false
                }


            }

            override fun afterTextChanged(editable: Editable) {

            }
        })


    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.txtViewConfirmButton ->

                if (hasUserGoiItAll) {
                    Toast.makeText(this, "Password successfully changed!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Please fill in all the fields above", Toast.LENGTH_SHORT).show()
                }
        }

    }
}
