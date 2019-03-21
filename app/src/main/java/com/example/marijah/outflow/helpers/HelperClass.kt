package com.example.marijah.outflow.helpers

import android.content.Context
import android.widget.Toast

const val TAG = "Marija"

fun showToast(context : Context, message : String)
{
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

}

public object categoryPickedObject{
    var categoryPicked = ""
}