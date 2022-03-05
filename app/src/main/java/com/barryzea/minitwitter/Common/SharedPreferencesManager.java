package com.barryzea.minitwitter.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private SharedPreferencesManager(){}
    private static final String PREFERENCE_FILE="PREFERENCE_SETTINGS";

    private static SharedPreferences getSharedPreference(){
        return MyApp.getContext().getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
    }
    public static void setSomeStringValue(String labelValue, String value){
        SharedPreferences.Editor editor=getSharedPreference().edit();
        editor.putString(labelValue, value);
        editor.apply();

    }
    public static void setSomeBooleanValue(String key, boolean value){
        SharedPreferences.Editor editor=getSharedPreference().edit();
        editor.putBoolean(key, value);
        editor.apply();

    }
    public static String getStringValuePreference(String key){
        return getSharedPreference().getString(key, null);
    }
    public static boolean getBooleanValuePreference(String key){
        return getSharedPreference().getBoolean(key, false);
    }

}
