package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

import org.json.JSONException;
import org.json.JSONObject;

public class Destination extends AppCompatActivity {
    private EditText destination;
    private TextView messageToUser;
    private String valUrl, dest;
    private Button btnOk;
    private boolean validationOk = false;
    private static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
    }

    private void initializeComponents() {
        setContentView(R.layout.activity_destination2);
        destination = findViewById(R.id.destination);
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destination.setText("");
                btnOk.setEnabled(false);
            }
        });
        destination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    dest = destination.getText().toString().trim();
                    valUrl = "https://bagpacker.pythonanywhere.com/validate/?destination="
                            + dest;
                    validate(valUrl);
                    handled = true;
                }
                return handled;
            }
        });
        messageToUser = findViewById(R.id.messageToUser);
        messageToUser.setText("");

        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed();
            }
        });
    }

    private void validate(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            validationOk = response.getBoolean("valid");
                            messageToUser.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            cancel();
                        }
                        if (validationOk) {
                            try {
                                String[] fullResponse = response.getString("destination").split(",");
                                String shortResponse = fullResponse[0] + "," + fullResponse[fullResponse.length - 1];
                                destination.setText(shortResponse);
                                btnOk.setEnabled(true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            destination.setText("");
                            messageToUser.setText("OGILTIG INMATNING \n(du måste ange en plats)");
                        }
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();
                        cancel();
                    }
                });
        queue.add(request);
        messageToUser.setText("Letar efter plats...");
    }

    private void proceed() {
        String url = "https://bagpacker.pythonanywhere.com/android/?param1=" + dest;
        Intent intent = new Intent(Destination.this, TripDate.class);
        intent.putExtra(URL, url);
        startActivity(intent);
    }

    private void cancel() {
        Intent intent = new Intent(Destination.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(Destination.this, "FEL VID HÄMTNING", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeComponents();
    }
}
