package se.mau.ai9856.bagpackerdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateTripActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText destination, param2, param3, param4;
    private TextView log;
    private String activity, accomodation, transport;
    private int itemPosition;
    private static final String ITEMS = "items";
    private ArrayList<String> items = new ArrayList<>();
    private Spinner spinnerAct;
    private Spinner spinnerAcc;
    private Spinner spinnerTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        destination = findViewById(R.id.destination);
        setUpSpinners();
        log = findViewById(R.id.log);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
            switch(parent.getId()){
                case R.id.spinner_activities:
                    activity = (String) parent.getSelectedItem();
                    itemPosition = parent.getSelectedItemPosition(); // skapa en position per spinner
                    break;
                case R.id.spinner_accomodation:
                    accomodation = (String) parent.getSelectedItem();
                    break;
                case R.id.spinner_transport:
                    transport = (String) parent.getSelectedItem();
                    break;
            }
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void generateList(View v) {
        if (!(destination.getText().length() > 0  || itemPosition > 0)) {
            log.setText("Du måste fylla i ALLA fält!");
        } else {
            String url = "http://bagpacker.pythonanywhere.com/android/?param1="
                    + destination.getText()
                    + "&param2=" + transport
                    + "&param3=" + accomodation
                    + "&param4=" + activity;
            //String header = "Till " + destination.getText();
            //items.add(header);
            String loading = "Laddar...";
            log.setText(loading);
            destination.setText(null);
            getJSON(url);
        }
    }

    public void getJSON(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("lista");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = jsonArray.getJSONObject(i);
                                String itemName = item.getString("item");
                                items.add(itemName);
                            }
                            showList(items);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();
                    }
                });
        queue.add(request);
    }

    public void showList(ArrayList<String> items) {
        Intent showListIntent = new Intent(this, ListViewActivity.class);
        showListIntent.putExtra(ITEMS, items);
        startActivity(showListIntent);
    }

    private void setUpSpinners(){
        spinnerAct = findViewById(R.id.spinner_activities);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                                    R.array.activities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAct.setAdapter(adapter);
        spinnerAct.setOnItemSelectedListener(this);

        spinnerAcc = findViewById(R.id.spinner_accomodation);
        ArrayAdapter<CharSequence> adapterAcc = ArrayAdapter.createFromResource(this,
                                    R.array.accomodation_array, android.R.layout.simple_spinner_item);
        adapterAcc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAcc.setAdapter(adapterAcc);
        spinnerAcc.setOnItemSelectedListener(this);

        spinnerTrans = findViewById(R.id.spinner_transport);
        ArrayAdapter<CharSequence> adapterTrans = ArrayAdapter.createFromResource(this,
                                    R.array.transport_array, android.R.layout.simple_spinner_item);
        adapterTrans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrans.setAdapter(adapterTrans);
        spinnerTrans.setOnItemSelectedListener(this);
    }
}
