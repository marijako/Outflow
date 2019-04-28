package com.example.marijah.outflow.popups

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.example.marijah.outflow.R
import kotlinx.android.synthetic.main.popup_leave_the_group.*

class LeaveTheGroupPopup(context: Context) : DimmedPopupDialog(context as Activity, R.layout.popup_leave_the_group) {

    var hasUserChosenToLeave: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        txtViewLeaveTheGroup.setOnClickListener {
            hasUserChosenToLeave = true
            dismiss()
        }

        txtViewCancel.setOnClickListener { dismiss() }

    }
}
