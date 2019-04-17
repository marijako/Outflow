package com.example.marijah.outflow.popups

import android.app.Activity
import android.os.Bundle
import com.example.marijah.outflow.R

class InvitePopup(private var activity: Activity, dialogLayoutResourceID: Int) : DimmedPopupDialog(activity, dialogLayoutResourceID) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}
