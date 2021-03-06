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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class Destination where the the user can write a destination for a trip. If the input
 * results in a hit the text  in the input field changes to the matched destination in format: city,
 * country.
 */


public class Destination extends AppCompatActivity {
    private EditText destination;
    private TextView messageToUser;
    private String valUrl, dest;
    private Button btnOk;
    private boolean validationOk = false;
    private static String shortResponse;
    private static final String URL = "url";
    private static final String DESTINATION_SAVE = "destinationToSave";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination2);
        initializeComponents();

        if (savedInstanceState != null) {
            dest = savedInstanceState.getString(DESTINATION_SAVE);
        //    destination.setText(dest);
        }
    }

    private void initializeComponents() {
        setContentView(R.layout.activity_destination2);
        btnOk = findViewById(R.id.btnNext);
        btnOk.setTextColor(ContextCompat.getColor(this, R.color.colorInputField));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed();
            }
        });
        btnOk.setEnabled(false);
        destination = findViewById(R.id.destination);
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

         //       destination.setText("");

                destination.setText("");
                btnOk.setTextColor(ContextCompat.getColor(Destination.this,
                        R.color.colorInputField));

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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                return handled;
            }
        });
        messageToUser = findViewById(R.id.messageToUser);
    //    messageToUser.setText("");
    }

    private void validate(String url) {
        final ProgressBar progressBar = findViewById(R.id.progressLoader);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            validationOk = response.getBoolean("valid");
                            messageToUser.setText("");
                            progressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            cancel();
                        }
                        if (validationOk) {
                            try {
                                String[] fullResponse = response.getString("destination").split(",");
                                shortResponse = fullResponse[0] + "," + fullResponse[fullResponse.length - 1];
                                destination.setText(shortResponse);
                                dest = fullResponse[0];
                                btnOk.setTextColor(ContextCompat.getColor(Destination.this,
                                        R.color.colorYellow));
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
        progressBar.setVisibility(View.VISIBLE);
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
        Toast.makeText(Destination.this, "ERROR: FEL VID HÄMTNING", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeComponents();
        destination.setText(dest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DESTINATION_SAVE, dest);

    }

    public static String getResponse(){
        return shortResponse;
    }
}
