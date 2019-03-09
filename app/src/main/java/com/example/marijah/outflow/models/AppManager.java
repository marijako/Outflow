package com.example.marijah.outflow.models;

import android.content.Context;
import android.content.SharedPreferences;

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
