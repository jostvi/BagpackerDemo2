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

public class ChoosePackinglistName extends AppCompatActivity {
    private Button btnOk;
    private EditText packinglistName;
    private TextView messageToUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_packinglist_name2);

        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(packinglistName.getText().length() > 0){
                    messageToUser.setText("Genererar packlista");
                    //           getJSON(url);

                }

            }
        });

     /*   public void getJSON(String url) {
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String json = response.toString();
                            showList(json);
                        }
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError e) {
                            e.printStackTrace();
                        }
                    });
            queue.add(request);
        }
        public void showList(String items) {
            Intent showListIntent = new Intent(ChoosePackinglistName.this, ListViewActivity.class);
            showListIntent.putExtra(ITEMS, items);
            startActivity(showListIntent);
        }*/
    }
}