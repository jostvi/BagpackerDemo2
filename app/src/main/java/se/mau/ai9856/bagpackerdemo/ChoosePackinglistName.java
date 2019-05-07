package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ChoosePackinglistName extends AppCompatActivity {
    private static final String URL = "url";
    private static final String ITEMS = "items";
    private static final String NAME = "name";
    private EditText packinglistName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_packinglist_name2);
        final String url = getIntent().getStringExtra(URL);
        packinglistName = findViewById(R.id.packinglistName);
        Button btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (packinglistName.getText().length() > 0) {
                    getJSON(url);
                }
            }
        });
    }

    public void getJSON(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        createExpandableList(response);
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();
                    }
                });
        queue.add(request);
        TextView messageToUser = findViewById(R.id.messageToUser);
        messageToUser.setText("Genererar packlista");
    }

    public void createExpandableList(JSONObject json) {
        LinkedHashMap<String, SubList> categorySubList = new LinkedHashMap<>();
        ArrayList<SubList> expList = new ArrayList<>();
        try {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String jsonString = gson.toJson(expList);
        Intent intent = new Intent(this, EditableListActivity.class);
        intent.putExtra(ITEMS, jsonString);
        intent.putExtra(NAME, packinglistName.getText().toString().trim());
        startActivity(intent);
    }

}