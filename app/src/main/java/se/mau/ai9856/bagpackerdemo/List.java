package se.mau.ai9856.bagpackerdemo;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class List {
    private String name;
    private String info;
    private String jsonString;

    public List(JSONObject json, String name, boolean infoAvailable){
        LinkedHashMap<String, SubList> categorySubList = new LinkedHashMap<>();
        ArrayList<SubList> expList = new ArrayList<>();
        this.name = name;
        try{
            if(infoAvailable){
                createInfoString(json);
            } else {
                info = "Detta är en gammal lista.\nAlltså finss ingen väderdata";
            }
            JSONArray jsonArray = json.getJSONArray("lista");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.getJSONObject(i);
                String category = jObject.getString("category");
                SubList subList = categorySubList.get(category);

                if (subList == null) {
                    subList = new SubList();
                    subList.setName(category);
                    categorySubList.put(category, subList);
                    expList.add(subList);
                }
                subList.addItem(new Packable(jObject.getString("item"),
                        Integer.parseInt(jObject.getString("quantity"))));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        Gson gson = new Gson();
        jsonString = gson.toJson(expList);
    }

    public String getName(){
        return name;
    }

    public String getInfo(){
        return info;
    }

    public String getJsonString(){
        return jsonString;
    }

    private void createInfoString(JSONObject json) throws JSONException {
        String dest = json.getString("destination");
        int minTemp = json.getInt("temp_min");
        int maxTemp = json.getInt("temp_max");
        // int length = json.getInt("length");
        String jsonWeather = json.getString("weather_data");
        info = "Packlistan för din resa till " + dest + " är baserad på " + jsonWeather
                + " väderdata. Temperaturen beräknas ligga mellan " + minTemp + " och "
                + maxTemp + " °C";
    }
}
