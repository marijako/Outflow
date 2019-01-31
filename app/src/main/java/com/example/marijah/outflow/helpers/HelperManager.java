package com.example.marijah.outflow.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

public class HelperManager {

    /**
     * Funkcija za postavljanje regularnog fonta
     * @param assetManager aseti
     * @param textView tekst view koji postavljamo
     */
    public static void setTypefaceRegular(AssetManager assetManager, TextView textView)
    {
        Typeface typeface = Typeface.createFromAsset(assetManager, "oswald_regular.ttf");
        textView.setTypeface(typeface);
    }

    /**
     * Funkcija za postavljanje lajt fonta
     * @param assetManager aseti
     * @param textView tekst view koji postavljamo
     */
    public static void setTypefaceLight(AssetManager assetManager, TextView textView)
    {
        Typeface typeface = Typeface.createFromAsset(assetManager, "oswald_light.ttf");
        textView.setTypeface(typeface);
    }



}
