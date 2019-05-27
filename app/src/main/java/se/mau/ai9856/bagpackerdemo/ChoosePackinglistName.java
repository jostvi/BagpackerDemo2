package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_packinglist_name2);
        initializeComponents();
    }

    private void initializeComponents(){
        TextView bulletDate=findViewById(R.id.page2);
        bulletDate.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletTransport=findViewById(R.id.page3);
        bulletTransport.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletAccommodation=findViewById(R.id.page4);
        bulletAccommodation.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletActivity=findViewById(R.id.page5);
        bulletActivity.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletTitle=findViewById(R.id.page6);
        bulletTitle.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        final String url = getIntent().getStringExtra(URL);
        packinglistName = findViewById(R.id.packinglistName);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setEnabled(false);
        btnNext.setTextColor(ContextCompat.getColor(this, R.color.colorInputField));
        final ProgressBar progressBar = findViewById(R.id.progressLoader);
        packinglistName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE && packinglistName.getText().length() > 0) {
                    handled = true;
                    btnNext.setEnabled(true);
                    btnNext.setTextColor(ContextCompat.getColor(ChoosePackinglistName.this,
                            R.color.colorYellow));
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                } else{
                    btnNext.setEnabled(false);
                    btnNext.setTextColor(ContextCompat.getColor(ChoosePackinglistName.this,
                            R.color.colorInputField));
                }
                return handled;
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    getJSON(url);
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
                        Toast.makeText(ChoosePackinglistName.this,
                                "Sorry, fel vid hämtning", Toast.LENGTH_LONG).show();
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
    }
}