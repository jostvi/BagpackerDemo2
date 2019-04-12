package com.example.project_bagpacker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;


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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class Packing_List extends AppCompatActivity {
    private EditText item;
    private Button btnAdd;
    private Button btnDel;
    private static final String ITEMS = "items";
    private ArrayList<String> items = new ArrayList<>();
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing__list);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnLogin = (Button)  findViewById(R.id.btnLogin);

        getJSON();
    }

    public void getJSON() { //btnLogin istället getListBtn
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        String url = "http://christina3107.eu.pythonanywhere.com/?param1=mobil&param2=mobil&param5=mobil";

      //  String url = "http://christina3107.eu.pythonanywhere.com/?json";
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
                            showList(items);
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
        //showList(items);
        //txtOut.setText(response.toString());
    }

    public void showList(ArrayList<String> items) {
        Intent showListIntent = new Intent(this, Packing_List2.class);
        showListIntent.putExtra(ITEMS, items);
        startActivity(showListIntent);
    }



}
