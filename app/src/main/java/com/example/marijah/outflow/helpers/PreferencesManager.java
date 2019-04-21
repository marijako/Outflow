package com.example.marijah.outflow.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class PreferencesManager {
    private static PreferencesManager instance;

    public SharedPreferences getPref() {
        return pref;
    }

    private final SharedPreferences pref;

    private PreferencesManager(Context context) {
        pref = context.getSharedPreferences(context.getPackageName() + ".PREF_NAME", Context.MODE_PRIVATE);
    }

    public static synchronized PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }


    public boolean isContain(String key) {
        return pref.contains(key);
    }

    public void setStringValue(String key, String value) {
        pref.edit().putString(key, value).apply();
    }

    public String getStringValue(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }


    public void setBooleanValue(String key, boolean value) {
        pref.edit().putBoolean(key, value).apply();
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        return pref.getBoolean(key, defaultValue);
    }

    public void setIntValue(String key, int value) {
        pref.edit().putInt(key, value).apply();
    }

    public void setLongValue(String key, long value) {
        pref.edit().putLong(key, value).apply();
    }

    public int getIntValue(String key, int defaultValue) {
        return pref.getInt(key, defaultValue);
    }

    public long getLongValue(String key, long defaultValue) {
        return pref.getLong(key, defaultValue);
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        pref.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        pref.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public void setStringList(String key, ArrayList<String> lists) {
        SharedPreferences.Editor editor = pref.edit();

        int size = pref.getInt(key, 0);

        for (int i = 0; i < size; i++) {
            editor.remove(key + String.valueOf(i));
        }

        if (size == lists.size()) {
            editor.putInt(key, 0);
        }

        editor.apply();

        editor.putInt(key, lists.size());
        for (int i = 0; i < lists.size(); i++) {
            editor.putString(key + String.valueOf(i), lists.get(i));
        }

        editor.apply();

    }

    public ArrayList<String> getStringList(String key) {
        ArrayList<String> list = new ArrayList<>();
        int size = pref.getInt(key, 0);
        for (int i = 0; i < size; i++) {
            list.add(pref.getString(key + String.valueOf(i), ""));
        }

        return list;
    }

    public void removeFromPreference(String key) {
        pref.edit().remove(key).apply();
    }
}
