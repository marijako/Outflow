package com.example.marijah.outflow.popups

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.DatePicker
import com.example.marijah.outflow.R
import kotlinx.android.synthetic.main.popup_date_picker.view.*
import android.app.AlertDialog.THEME_HOLO_LIGHT
import android.app.DatePickerDialog
import com.example.marijah.outflow.helpers.showToast
import java.util.*


class DatePickerPopup(private var activity: Activity, dialogLayoutResourceID: Int) : DimmedPopupDialog(activity, dialogLayoutResourceID) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(activity,
                AlertDialog.THEME_HOLO_LIGHT, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth -> showToast(activity, "Hello ") }, 2019, 1, 1)
    }

}

