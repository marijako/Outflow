package com.example.marijah.outflow.helpers

import android.content.res.AssetManager
import android.graphics.Typeface
import android.widget.TextView

object HelperManager {

    /**
     * Funkcija za postavljanje regularnog fonta
     * @param assetManager aseti
     * @param textView tekst view koji postavljamo
     */
    fun setTypefaceRegular(assetManager: AssetManager, textView: TextView) {
        val typeface = Typeface.createFromAsset(assetManager, "oswald_regular.ttf")
        textView.typeface = typeface
    }

    /**
     * Funkcija za postavljanje lajt fonta
     * @param assetManager aseti
     * @param textView tekst view koji postavljamo
     */
    fun setTypefaceLight(assetManager: AssetManager, textView: TextView) {
        val typeface = Typeface.createFromAsset(assetManager, "oswald_light.ttf")
        textView.typeface = typeface
    }


}
