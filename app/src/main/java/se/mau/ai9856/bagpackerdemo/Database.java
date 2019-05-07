package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static void saveList(Context context, String key, ArrayList<SubList> expList) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((context));
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(expList);
        editor.putString(key, json);
        editor.apply();
    }

    public static ArrayList<SubList> loadList(Context context, String key) {
        ArrayList<SubList> list;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String response = prefs.getString(key, "");
        list = gson.fromJson(response, new TypeToken<List<SubList>>(){}.getType());

        return list;
    }

    public static void deleteList(Context context, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void saveName(Context context, String key, String name){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((context));
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, name);
        editor.apply();
    }

    public static String loadName(Context context, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, "");
    }

    public static void deleteName(Context context, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }
}
