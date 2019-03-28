package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    /*private EditText inputTxt;
    /private TextView txtOut;
    private String[] stuff = {"Badkläder","Strumpor","T-Shirt","Gummistövlar","Tandborste",
            "Chokladkartong","Lusekofta","Pyjamas","Pass","Astmaspray","Toalettborste",
            "Badanka","Laddare","Sladdar","Midjeväska","PingPongBoll","Badboll",
            "Underbyxor","Monokel","Damasker" };*/
    private static final String ITEMS = "items";
    private ArrayList<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inputTxt = findViewById(R.id.inputTxt);
        //txtOut = findViewById(R.id.textView2);
    }

    public void getJSON(View getListBtn) {
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        String url = "http://christina3107.eu.pythonanywhere.com/?json";
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject item = jsonArray.getJSONObject(i);
                                String itemName = item.getString("item");
                                items.add(itemName);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError e){
                        e.printStackTrace();
                    }
                });
        requestQueue.add(request);
        showList(items);
        //txtOut.setText(response.toString());
    }

    public void showList(ArrayList<String> items){
        Intent showListIntent = new Intent(this, Activity2.class);
        showListIntent.putExtra(ITEMS, items);
        startActivity(showListIntent);
        /*String destination = inputTxt.getText().toString();

        if(destination.equalsIgnoreCase("Gävle")) {
            String[] toGefle = {stuff[1],stuff[3],stuff[7],stuff[17]};
            showListIntent.putExtra(ITEMS, toGefle);

        }
        else if(destination.equalsIgnoreCase("Töreboda")) {
            showListIntent.putExtra(ITEMS, stuff);
        }
        else{
            String[] toAnywhere = {"Handduk"};
            showListIntent.putExtra(ITEMS, toAnywhere);
        }
        startActivity(showListIntent);*/
    }
}
