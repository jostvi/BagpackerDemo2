package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

/**
 * This is where the application starts. From here, the user can choose to retrieve
 * a list from a dummy webb-server then go to ListViewActivity, or go to CreateTripActivity to
 * create a new list, or to..
 *
 * @author Johan W
 */

public class MainActivity extends AppCompatActivity {
    private Button btnGetList;
    private static final String ITEMS = "items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetList = findViewById(R.id.getListBtn); // ändra id i xml-koden
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateNewAccount.class);
                startActivity(intent);
            }
        });
        Button btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }

    public void getChristinasJSON(View v) {   // byt namn
        EditText input = findViewById(R.id.password_input); // ändra id i xml
        String password = input.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://bagpacker.pythonanywhere.com/get_list/?param1=" + password;

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String jsonStr = response.toString();
                        showList(jsonStr);
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

    public void showList(String items) {
        Intent showListIntent = new Intent(this, ListViewActivity.class);
        showListIntent.putExtra(ITEMS, items);
        startActivity(showListIntent);
    }

    public void createTrip(View v) {
        Intent createTripIntent = new Intent(this, CreateTripActivity.class);
        startActivity(createTripIntent);
    }

    public void showSavedLists(View v) {
        Intent intent = new Intent(this, SavedLists.class);
        startActivity(intent);
    }

    public void testJSON(View v){
        String jsonStr = "{\"lista\":[{\"item\":\"skor\",\"category\":\"kläder\"}," +
                "                     {\"item\":\"tisha\",\"category\":\"kläder\"}," +
                "                     {\"item\":\"tights\",\"category\":\"kläder\"}," +
                "                     {\"item\":\"machete\",\"category\":\"vapen\"}," +
                "                     {\"item\":\"hjärnblödning\",\"category\":\"tillstånd\"}]}";
        showList(jsonStr);
    }
}