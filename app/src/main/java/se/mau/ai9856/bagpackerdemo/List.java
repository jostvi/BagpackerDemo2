package se.mau.ai9856.bagpackerdemo;

import android.util.Log;
import android.widget.Toast;

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
    private float totalWeight = 0;

    public List(JSONObject json, String name, boolean isNewList){
        LinkedHashMap<String, SubList> categorySubList = new LinkedHashMap<>();
        ArrayList<SubList> expList = new ArrayList<>();
        this.name = name;
        try{
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
                Packable item = new Packable(jObject.getString("item"),
                        Integer.parseInt(jObject.getString("quantity")),
                        Float.parseFloat(jObject.getString("weight")));
                if(!isNewList){
                    if(jObject.getInt("checked") == 1)
                        item.isSelected = true;
                    else if (jObject.getInt("checked") == 0)
                        item.isSelected = false;
                }
                totalWeight += (item.getQuantity() * item.getWeight());
                subList.addItem(item);
            }
            createInfoString(json, isNewList);

        }catch(JSONException e){
            e.printStackTrace();
            Log.e("ERROR", "Fel vid hämtning av JSON");
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

    private void createInfoString(JSONObject json, boolean isNew) throws JSONException {
        String dest;
        if(isNew){
            dest = Destination.getResponse();
        } else{
            dest = json.getString("destination");
        }
        int minTemp = json.getInt("temp_min");
        int maxTemp = json.getInt("temp_max");
        //String weatherData = json.getString("weather_data");
        String rain;/*
        if(weatherData.equals("aktuell")){
            boolean isRaining = json.getBoolean("rain");
            if(isRaining)
                rain = "hög";
            else
                rain = "låg";
        } else{*/
            int isRaining = json.getInt("rain");
            if(isRaining == 0)
                rain = "låg";
            else if(isRaining == 1)
                rain = "hög";
            else
                rain = "oklar";
        //}

        info = dest + "\n" + TripDate.getStartDate() + " - " + TripDate.getEndDate() + "\n" +
                "min: " + minTemp + " °C, max: " + maxTemp + " °C\n" +
                "Regnrisk: " + rain + "\n" + "Totalvikt ca: " + String.format("%.2f", totalWeight);
    }
}
