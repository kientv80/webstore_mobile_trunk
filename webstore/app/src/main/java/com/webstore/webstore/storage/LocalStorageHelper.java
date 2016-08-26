package com.webstore.webstore.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LAP10572-local on 7/8/2016.
 */
public class LocalStorageHelper {

    private static LocalStorageHelper instance;
    private static SharedPreferences sharedPref = null;
    public static void init(Context context){
        if(sharedPref == null)
         sharedPref = context.getSharedPreferences("webstore.config.APP_CONFIG", Context.MODE_PRIVATE);
    }
    public static void saveToFile(String key, String value){
        if(sharedPref != null){
            SharedPreferences.Editor edit = sharedPref.edit();
            edit.putString(key, value);
            edit.commit();
        }
    }
    public static String getFromFile(String key){
        return sharedPref.getString(key,"");
    }
}
