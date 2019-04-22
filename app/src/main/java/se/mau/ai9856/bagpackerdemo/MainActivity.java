package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

/**
 * This is where the application starts. From here, the user can choose to retrieve
 * a list from a dummy webb-server then go to ListViewActivity, or go to CreateTripActivity to
 * create a new list, or to..
 * @author Johan W
 */
public class MainActivity extends AppCompatActivity {
    private Button btnGetList;
    private static final String ITEMS = "items";
    private ArrayList<String> items = new ArrayList<>();

    private Button btnCreateList;
    private Button btnLogin;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetList = findViewById(R.id.getListBtn);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnCreateAccount = (Button)findViewById(R.id.btnCreateAccount);
        btnCreateList = (Button)findViewById(R.id.btnCreateList);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CreateNewAccount.class );
                startActivity(intent);

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() { //btnLogin

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);

            }
        });


    }

    public void getJSON(View v) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://bagpacker.pythonanywhere.com/get_list/?param1=123";

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
        requestQueue.add(request);
        String loading = "laddar...";
        btnGetList.setText(loading);
    }

    public void showList(ArrayList<String> items) {

        Intent showListIntent = new Intent(this, ListViewActivity.class);
        showListIntent.putExtra(ITEMS, items);
        startActivity(showListIntent);
    }

    public void createTrip(View v) {
        Intent createTripIntent = new Intent(this, CreateTripActivity.class);
        startActivity(createTripIntent);
    }

    public void showSavedLists(View v){
        Intent intent = new Intent (this, SavedLists.class);
        startActivity(intent);
    }
}