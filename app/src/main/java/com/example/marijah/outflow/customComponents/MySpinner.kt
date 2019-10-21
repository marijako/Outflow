package com.example.marijah.outflow.customComponents

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner


class MySpinner(context: Context, attrs: AttributeSet) : Spinner(context, attrs) {

    private var listener: OnItemSelectedListener? = null

    override fun setSelection(position: Int) {
        super.setSelection(position)

        if (position == selectedItemPosition) {
            listener?.onItemSelected(null, null, position, 0)
        }
    }

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        this.listener = listener
    }
}