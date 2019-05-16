package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class ChoosePackinglistName extends AppCompatActivity {
    private static final String URL = "url";
    private static final String ITEMS = "items";
    private static final String NAME = "name";
    private static final String INFO = "info";
    private EditText packinglistName;
    private TextView messageToUser;
    private ImageButton arrowBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_packinglist_name2);
        final String url = getIntent().getStringExtra(URL);
        packinglistName = findViewById(R.id.packinglistName);
        messageToUser = findViewById(R.id.messageToUser);

        arrowBack = findViewById(R.id.arrowBack);
        arrowBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoosePackinglistName.this, TripActivity.class);
                startActivity(intent);

            }
        });
        Button btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (packinglistName.getText().length() > 0

                     && !packinglistName.getText().toString().startsWith(" ")
                        && packinglistName.getText().length() < 16) {
                    messageToUser.setText("Genererar packlista");
                    getJSON(url);
                } else {
                    messageToUser.setText("Ange packlistans namn som är mindre än 15 tecken");

                }
            }
        });
    }

    public void getJSON(String url) {
        final String name = packinglistName.getText().toString().trim();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List list = new List(response, name, true);
                        showExpandableList(list);
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();
                        messageToUser.setText("");
                        Toast.makeText(ChoosePackinglistName.this,
                                "Fel vid hämtning\nFörsök igen senare", Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(request);
    }

    public void showExpandableList(List list) {
        Intent intent = new Intent(this, EditableListActivity.class);
        intent.putExtra(ITEMS, list.getJsonString());
        intent.putExtra(NAME, list.getName());
        intent.putExtra(INFO, list.getInfo());
        startActivity(intent);
        //List list = new List(json, packinglistName.getText().toString().trim());
        /*LinkedHashMap<String, SubList> categorySubList = new LinkedHashMap<>();
        ArrayList<SubList> expList = new ArrayList<>();
        String infoString = "";

        try {
            JSONArray jsonArray = json.getJSONArray("lista");

            String dest = json.getString("destination");
            int minTemp = json.getInt("temp_min");
            int maxTemp = json.getInt("temp_max");
            // int length = json.getInt("length");
            String jsonWeather = json.getString("weather_data");
            infoString = "Packlistan för din resa till " + dest + " är baserad på " + jsonWeather
                    + " väderdata. Temperaturen beräknas ligga mellan " + minTemp + " och "
                    + maxTemp + " °C";

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
        String jsonString = gson.toJson(expList);*/
    }

}