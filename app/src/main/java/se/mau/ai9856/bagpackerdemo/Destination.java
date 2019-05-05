package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class Destination extends AppCompatActivity {
    private TextView questionDestination;
    private EditText destination;
    private TextView messageToUser;
    private Button btnOk;
    private static final String URL = "url";

   // private static final String DESTINATION = "destination";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        questionDestination = findViewById(R.id.questionDestination);
        destination= findViewById(R.id.destination);
        btnOk = findViewById(R.id.btnOk);
        messageToUser = findViewById(R.id.messageToUser);

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (destination.getText().length() == 0) {
                    messageToUser.setText("Ange resemål");
                }else{
                    String dest = destination.getText().toString();
                    dest = dest.trim();
                    String url = "http://bagpacker.pythonanywhere.com/android/?param1="
                            + dest;

                          /*  + "&param2=" + "" //tripDate
                            + "&param3=" + "" //transport
                            + "&param4=" + "" //accomodation
                            + "&param5=" + ""; //tripactivity*/



                    Intent intent = new Intent(Destination.this,TripDate.class );
                    intent.putExtra(URL, url);
                    startActivity(intent);


                }

            }
        });

    }



}
