package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static void saveList(Context context, String key, ArrayList<SubList> expList) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((context));
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("SAVED_LISTS", key);
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

        if(response == null){
            SubList emptyList = new SubList();
            emptyList.setName("Inga sparade listor :(");
            list = new ArrayList<>();
            list.add(emptyList);
        } else {
            list = gson.fromJson(response, new TypeToken<List<SubList>>(){}.getType());
        }
        return list;
    }
}
