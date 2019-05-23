package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

/**
 * This is where the application starts. From here, the user can choose to retrieve
 * a list from a dummy webb-server then go to EditableListActivity, or go to CreateTripActivity to
 * create a new list, or to..
 *
 * @author Johan W
 */

public class MainActivity extends AppCompatActivity {
    private static final String ITEMS = "items";
    private static final String NAME = "name";
    private static final String INFO = "info";

    @Override
    public void onBackPressed(){
        finishAffinity();
        finish();
    }

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
        EditText codeInput = findViewById(R.id.password_input);
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

        codeInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getListFromServer();
                    handled = true;
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return handled;
            }
        });
    }



    public void getListFromServer() {
        EditText codeInput = findViewById(R.id.password_input);
        final String password = codeInput.getText().toString().trim();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://bagpacker.pythonanywhere.com/get_list/?param1=" + password;

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List list = new List(response, "Webblista: " + password, false);
                        showExpandableList(list);
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Fel vid hämtning " +
                                "\nSkrev du rätt lösenord?", Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

    public void showExpandableList(List list) {
        Intent intent = new Intent(this, EditableListActivity.class);
        intent.putExtra(ITEMS, list.getJsonString());
        intent.putExtra(NAME, list.getName());
        intent.putExtra(INFO, list.getInfo());
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
}