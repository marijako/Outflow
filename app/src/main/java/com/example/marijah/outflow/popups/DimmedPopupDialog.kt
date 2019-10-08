package com.example.marijah.outflow.popups

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.example.marijah.outflow.R

open class DimmedPopupDialog(activity: Activity, private val dialogLayoutResourceID: Int) : Dialog(activity, R.style.full_screen_dialog) {

    private val dimmedBackgroundColor: Int = Color.BLACK
    private val dimmedBackgroundAlpha: Float = 0.7f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(dialogLayoutResourceID)
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)

        val flContent = findViewById<FrameLayout>(android.R.id.content)

        val rlDimmedBackground = RelativeLayout(context)
        rlDimmedBackground.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        rlDimmedBackground.setBackgroundColor(dimmedBackgroundColor)
        rlDimmedBackground.alpha = dimmedBackgroundAlpha

        flContent.addView(rlDimmedBackground, 0)
    }

}