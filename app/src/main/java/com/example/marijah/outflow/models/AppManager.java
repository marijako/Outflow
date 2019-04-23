package com.example.marijah.outflow.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.marijah.outflow.helpers.PreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AppManager {

    private static AppManager ourInstance;
    private Context mContext;


    private AppManager(Context context) {
        mContext = context;
    }


    public static AppManager getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new AppManager(context);
        }
        return ourInstance;
    }


    // S H A R E D   P R E F E R E N C E S

    /**
     * Funkcija postavljanje flega da korisnik nije usao prvi put u aplikaciju
     */
    public void setUserEnteredInAppForTheFirstTime() {
        PreferencesManager.getInstance(mContext).setBooleanValue("USER_ENTERED_FOR_THE_FIRST_TIME", false);
    }

    /**
     * Funkcija za dobijanje istine o tome da li je korisnik prvi put uplikaciji
     */
    public boolean getUserEnteredInAppForTheFirstTime() {
        return PreferencesManager.getInstance(mContext).getBooleanValue("USER_ENTERED_FOR_THE_FIRST_TIME", true);
    }


    /**
     * Funkcija za postavljanje imena trenutno osluskivane tabele
     * @param tableName ime tabele
     */
    public void setCurrentlyLookedTableName(String tableName) {
        PreferencesManager.getInstance(mContext).setStringValue("CURRENT_TABLE_NAME", tableName);
    }

    /**
     * Funkcija za postavljanje imena trenutno osluskivane tabele
     */
    public String getCurrentlyLookedTableName() {
        return PreferencesManager.getInstance(mContext).getStringValue("CURRENT_TABLE_NAME", "");
    }

    /**
     * Funkcija za pamcenje trenutno logovanog korisnika
     * @param userEmail korisnikov imejl
     */
    public void setCurrentlyLoggedInUserEmail(String userEmail) {
        PreferencesManager.getInstance(mContext).setStringValue("CURRENTLY_LOGGED_IN_EMAIL", userEmail);
    }

    /**
     * Funkcija za dobijanje trenutno logovanog korisnika
     */
    public String getCurrentlyLoggedInUserEmail() {
        return PreferencesManager.getInstance(mContext).getStringValue("CURRENTLY_LOGGED_IN_EMAIL", "");
    }


    // S H A R E D   P R E F E R E N C E S   E N D









    public ArrayList getArrayList(String key) {
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName() + ".PREF_NAME", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void saveArrayList(ArrayList list, String key) {
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName() + ".PREF_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }






}
