package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

public class TripActivity extends AppCompatActivity {
    private Button btnOk;
    private ArrayList<String> selection = new ArrayList<String>();
    private String url;
    private static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                url = getIntent().getStringExtra(URL);
                url+="&param6="+selection;
                Log.e("hej ", "url" + url);
                Intent intent = new Intent(TripActivity.this,ChoosePackinglistName.class );
                startActivity(intent);

            }
        });
    }

    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.checkOutdoor:
                if(checked){
                    selection.add("Friluftsliv");
                }else{
                    selection.remove("Friluftsliv");
                }
                break;

            case R.id.checkWintersport:
                if(checked){
                    selection.add("Vintersport");
                }else{
                    selection.remove("Vintersport");
                }
                break;

            case R.id.checkBeach:
                if(checked){
                    selection.add("Sol och strand");
                }else{
                    selection.remove("Sol och strand");
                }
                break;

            case R.id.checkSightseeing:
                if(checked){
                    selection.add("Sightseeing");
                }else{
                    selection.remove("Sightseeing");
                }
                break;

            case R.id.checkCultureNightlife:
                if(checked){
                    selection.add("Kultur- och nattliv");
                }else{
                    selection.remove("Kultur- och nattliv");
                }
                break;
        }
    }
}
