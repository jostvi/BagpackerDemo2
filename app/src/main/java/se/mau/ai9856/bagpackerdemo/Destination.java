package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    private String url, valUrl, dest;
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
        Button btnOk = findViewById(R.id.btnOk);
        messageToUser = findViewById(R.id.messageToUser);
        messageToUser.setText("");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dest = destination.getText().toString().trim();
                valUrl = "https://bagpacker.pythonanywhere.com/validate/?destination="
                        + dest;
                char[] chars = dest.toCharArray();
                boolean containNums = false;

                for (int i = 0; i < (chars.length - 1) && (!containNums); i++) {
                    if ("0123456789".indexOf(chars[i]) >= 0) {
                        containNums = true;
                    }
                }
                if (containNums) {
                    destination.setText("");
                    messageToUser.setText("OGILTIG INMATNING \n(siffror är INTE ok)");
                } else {
                    validate(valUrl);
                }
            }
        });
    }

    private void validate(String url) {
        /*PlacesClient places = new PlacesClient("OFFLENX550",
                "03915e4200417c80047eaf5c6451301b");
        PlacesQuery query = new PlacesQuery();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line
        ,query.;
        destination.setAdapter(adapter);

        places.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                destination.showDropDown();
            }
        });*/
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            validationOk = response.getBoolean("destination");
                        } catch (JSONException e) {
                            cancel();
                            e.printStackTrace();
                        }
                        if (validationOk) {
                            proceed();
                        } else {
                            messageToUser.setText("");
                            destination.setText("");
                            messageToUser.setText("OGILTIG INMATNING \n(du måste ange en stad)");
                        }
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError e) {
                        cancel();
                        e.printStackTrace();
                    }
                });
        queue.add(request);
        messageToUser.setText("Validerar...");
    }

    private void proceed() {
        url = "https://bagpacker.pythonanywhere.com/android/?param1=" + dest;
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
