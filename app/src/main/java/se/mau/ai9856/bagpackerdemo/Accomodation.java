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
    private ArrayList<String> selection = new ArrayList<String>();
    private String url;
    private static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation2);

        Button btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                url = getIntent().getStringExtra(URL);
                url+="&param5="+selection;
                /*url = getIntent().getStringExtra(URL) + "&param5=[";
                for(String string : selection){
                    url += "'" + string + "'" + ",";
                }
                url = url.substring(0, url.length()-1);
                url += "]";*/
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
                    selection.add("hotel");
                }else{
                    selection.remove("hotel");
                }
                break;

            case R.id.checkApartment:
                if(checked){
                    selection.add("apartment");
                }else{
                    selection.remove("apartment");
                }
                break;

            case R.id.checkWithFriend:
                if(checked){
                    selection.add("friends");
                }else{
                    selection.remove("friends");
                }
                break;

            case R.id.checkCaravan:
                if(checked){
                    selection.add("camper");
                }else{
                    selection.remove("camper");
                }
                break;

            case R.id.checkBike:
                if(checked){
                    selection.add("bike");
                }else{
                    selection.remove("bike");
                }
                break;

            case R.id.checkTent:
                if(checked){
                    selection.add("tent");
                }else{
                    selection.remove("tent");
                }
                break;

            case R.id.checkCottage:
                if(checked){
                    selection.add("cottage");
                }else{
                    selection.remove("cottage");
                }
                break;




        }
    }
}