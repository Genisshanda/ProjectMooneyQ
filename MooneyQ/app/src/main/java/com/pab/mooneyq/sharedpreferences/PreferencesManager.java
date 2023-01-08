package com.pab.mooneyq.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public PreferencesManager(Context context) {
        sharedPref = context.getSharedPreferences("MooneyQ.pref",
                Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void put(String key, Boolean value) {
        editor.putBoolean(key, value)
                .apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPref.getBoolean(key, false);
    }

    public void put(String key, String value) {
        editor.putString(key, value)
                .apply();
    }

    public String getString(String key) {
        return sharedPref.getString(key, "");
    }

    public void put(String key, Integer value) {
        editor.putInt(key, value)
                .apply();
    }

    public Integer getInt(String key) {
        return sharedPref.getInt(key, 0);
    }

    public void clear(){
        editor.putBoolean("pref_is_login", false)
                .apply();
//        editor.clear()
//                .apply();
    }
}
