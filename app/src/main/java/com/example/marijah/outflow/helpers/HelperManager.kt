package com.example.marijah.outflow.helpers

import android.content.Context
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

    /**
     * Funkcija koja generise novo ime tabele koju korisnik osluskuje
     */
    fun setUpNewTableName(email1: String, email2: String): String {
        // upredjujemo ova dva stringa
        val compare = email1.compareTo(email2)

        if (compare < 0) {
            return "expenses_${email2}_$email1"
        } else if (compare > 0) {
            return "expenses_${email1}_$email2"
        } else {
            // ne mogu nikad biti isti
            return "expenses_${email1}_$email2"
        }

    }


    fun replaceTheLastOccurrenceOfTheSubstringInAString(toReplace: String, replaceWith: String, editedString: String): String {

        val originalString = editedString
        val substring = toReplace
        val replacement = replaceWith
        val positionOfTheSubstring = editedString.lastIndexOf(substring)

        val builder = StringBuilder()
        builder.append(originalString.substring(0, positionOfTheSubstring))
        builder.append(replacement)
        builder.append(originalString.substring(positionOfTheSubstring + substring.length))

        return builder.toString()

    }

    /**
     * Funkcija za sortiranje arrayListe stringova
     * Sluzi nam za dobijanje novog naziva tabele.
     */
    fun sortArrayListOfStringsAndReturnThatString(arrayListToBeSorted: ArrayList<String>): String {

        var sortedStrings = ""
        arrayListToBeSorted.sort()

        for (word in arrayListToBeSorted) {
            sortedStrings += "_$word"
        }

        return sortedStrings
    }

    /**
     * Funkcija za postavljanje fonta u celoj aplikaciji.
     * Primenjujemo ovaj pristup jer fontFamily pristup je za novije apije.
     */
    fun setDefaultFont(context: Context, staticTypefaceFieldName: String, fontAssetName: String) {
        val regular = Typeface.createFromAsset(context.assets, fontAssetName)
        replaceFont(staticTypefaceFieldName, regular)
    }

    private fun replaceFont(staticTypefaceFieldName: String, newTypeface: Typeface) {
        try {
            val staticFiled = Typeface::class.java.getDeclaredField(staticTypefaceFieldName)
            staticFiled.isAccessible = true
            staticFiled.set(null, newTypeface)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


}
