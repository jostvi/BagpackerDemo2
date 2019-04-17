package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class Database {

    public static void saveList(Context context, String key, ArrayList<String> list){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((context));
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (String item : list){
            a.put(item);
        }
        if (!list.isEmpty()){
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    public static ArrayList<String> loadList(Context context, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> items = new ArrayList<>();
        if (json != null){
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++){
                    String item = a.optString(i);
                    items.add(item);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return items;
    }
}
