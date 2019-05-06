package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class CreateTripActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String ITEMS = "items";
    private LinkedHashMap<String, SubList> categorySubList = new LinkedHashMap<>();
    private ArrayList<SubList> expList = new ArrayList<>();
    private EditText destination;
    private TextView log;
    private String activity, accommodation, transport;
    private int itemPosition;

    @Override
    public void onRestart(){
        super.onRestart();
        initializeComponents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
    }

    private void initializeComponents(){
        setContentView(R.layout.activity_create_trip);
        destination = findViewById(R.id.destination);
        setUpSpinners();
        log = findViewById(R.id.showDateFrom);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
        switch (parent.getId()) {
            case R.id.spinner_activities:
                activity = (String) parent.getSelectedItem();
                itemPosition = parent.getSelectedItemPosition(); // skapa en position per spinner
                break;
            case R.id.spinner_accomodation:
                accommodation = (String) parent.getSelectedItem();
                break;
            case R.id.spinner_transport:
                transport = (String) parent.getSelectedItem();
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void generateList(View v) {
        if (!(destination.getText().length() > 0) || ! (itemPosition > 0)) { //setDestination istället destination
            String error = "Du måste fylla i ALLA fält!";
            log.setText(error);
        } else {
            String url = "https://bagpacker.pythonanywhere.com/android/?param1="
                                                        + destination.getText()
                                                        + "&param2=" + transport
                                                        + "&param3=" + accommodation
                                                        + "&param4=" + activity;
            String loading = "Laddar...";
            log.setText(loading);
            destination.setText(null);
            getJSON(url);
            //  Log.e("hej ", "url" + url);

        }
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
    }

    public void createExpandableList(JSONObject json) {
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
                int quantity = 1; // TEST
                subList.addItem(new Packable(jObject.getString("item"), quantity));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String jsonString = gson.toJson(expList);
        Intent intent = new Intent(this, EditListActivity.class);
        intent.putExtra(ITEMS, jsonString);

        startActivity(intent);
    }

    private void setUpSpinners() {
        Spinner spinnerAct = findViewById(R.id.spinner_activities);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAct.setAdapter(adapter);
        spinnerAct.setOnItemSelectedListener(this);

        Spinner spinnerAcc = findViewById(R.id.spinner_accomodation);
        ArrayAdapter<CharSequence> adapterAcc = ArrayAdapter.createFromResource(this,
                R.array.accomodation_array, android.R.layout.simple_spinner_item);
        adapterAcc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAcc.setAdapter(adapterAcc);
        spinnerAcc.setOnItemSelectedListener(this);

        Spinner spinnerTrans = findViewById(R.id.spinner_transport);
        ArrayAdapter<CharSequence> adapterTrans = ArrayAdapter.createFromResource(this,
                R.array.transport_array, android.R.layout.simple_spinner_item);
        adapterTrans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrans.setAdapter(adapterTrans);
        spinnerTrans.setOnItemSelectedListener(this);
    }
}
