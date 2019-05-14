package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * This is where the application starts. From here, the user can choose to retrieve
 * a list from a dummy webb-server then go to EditableListActivity, or go to CreateTripActivity to
 * create a new list, or to..
 *
 * @author Johan W
 */

public class MainActivity extends AppCompatActivity {
    private Button btnGetList;
    private static final String ITEMS = "items";
    private LinkedHashMap<String, SubList> categorySubList = new LinkedHashMap<>();
    private ArrayList<SubList> expList = new ArrayList<>();

    @Override
    public void onRestart() {
        super.onRestart();
        initializeComponents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
    }

    private void initializeComponents() {
        setContentView(R.layout.activity_main);
        btnGetList = findViewById(R.id.getListBtn);
        btnGetList.setText("Hämta lista");
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateNewAccount.class);
                startActivity(intent);
            }
        });
        Button btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }

    public void getChristinasJSON(View v) {
        EditText input = findViewById(R.id.password_input);
        String password = input.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://bagpacker.pythonanywhere.com/get_list/?param1=" + password;

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

        requestQueue.add(request);
        String loading = "laddar...";
        btnGetList.setText(loading);
    }

    private void createExpandableList(JSONObject json) {
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

        startActivity(intent);
    }

    public void createTrip(View v) {
        Intent createTripIntent = new Intent(this, Destination.class);
        startActivity(createTripIntent);
    }

    public void showSavedLists(View v) {
        Intent intent = new Intent(this, ShowSavedListActivity.class);
        startActivity(intent);
    }

    public void testJSON(View v) {
        String jsonStr = "{\"lista\":[{\"item\":\"skor\",\"category\":\"kläder\"}," +
                "                     {\"item\":\"tisha\",\"category\":\"kläder\"}," +
                "                     {\"item\":\"tights\",\"category\":\"kläder\"}," +
                "                     {\"item\":\"machete\",\"category\":\"vapen\"}," +
                "                     {\"item\":\"hjärnblödning\",\"category\":\"tillstånd\"}]}";
    }
}