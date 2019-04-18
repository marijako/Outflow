package com.example.marijah.outflow.popups

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.popup_delete.*

class DeletePopup(private var activity: Activity, dialogLayoutResourceID: Int) : DimmedPopupDialog(activity, dialogLayoutResourceID) {

    var hasUserClickedDelete: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        txtViewDeleteRecord.setOnClickListener {
            hasUserClickedDelete = true
            dismiss()
        }

        txtViewCancel.setOnClickListener { dismiss() }

    }

}
