package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

public class Accomodation extends AppCompatActivity {
    private Button btnOk;
    private ArrayList<String> selection = new ArrayList<String>();
    private String url;
    private static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation2);

        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                url = getIntent().getStringExtra(URL);
                url+="&param5="+selection;
                Log.e("hej ", "url" + url);
                Intent intent = new Intent(Accomodation.this,TripActivity.class );
                intent.putExtra(URL, url);
                startActivity(intent);

            }
        });
    }

    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.checkHotel:
                if(checked){
                    selection.add("Hotell");
                }else{
                    selection.remove("Hotell");
                }
                break;

            case R.id.checkApartment:
                if(checked){
                    selection.add("Lägenhet");
                }else{
                    selection.remove("Lägenhet");
                }
                break;

            case R.id.checkWithFriend:
                if(checked){
                    selection.add("Hos kompisar");
                }else{
                    selection.remove("Hos kompisar");
                }
                break;

            case R.id.checkCaravan:
                if(checked){
                    selection.add("Husvagn");
                }else{
                    selection.remove("Husvagn");
                }
                break;

            case R.id.checkBike:
                if(checked){
                    selection.add("Cykel");
                }else{
                    selection.remove("Cykel");
                }
                break;

            case R.id.checkTent:
                if(checked){
                    selection.add("Tält");
                }else{
                    selection.remove("Tält");
                }
                break;

            case R.id.checkCottage:
                if(checked){
                    selection.add("Stuga");
                }else{
                    selection.remove("Stuga");
                }
                break;




        }
    }
}