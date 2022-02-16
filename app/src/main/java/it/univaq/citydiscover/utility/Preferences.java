package it.univaq.citydiscover.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static void save(Context context, String key, boolean value){
        SharedPreferences preferences= context.getSharedPreferences("myPref",Context.MODE_PRIVATE);  //private perchè deve essere leggibile soltanto dall'applicazione
        preferences.edit().putBoolean(key,value).apply();
    }

    public static boolean load(Context context,String key, boolean defValue){
        SharedPreferences preferences= context.getSharedPreferences("myPref",Context.MODE_PRIVATE);
         return preferences.getBoolean(key,defValue);
    }
}
