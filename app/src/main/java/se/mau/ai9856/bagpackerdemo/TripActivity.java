package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class TripActivity extends AppCompatActivity {

    private CheckBox checkOutdoor;
    private CheckBox checkWintersport;
    private CheckBox checkBeach;
    private CheckBox checkSightseeing;
    private CheckBox checkCulture;
    private CheckBox checkNightlife;
    private CheckBox checkTraining;
    private CheckBox checkOther;
    private TextView messageToUser;
    private ArrayList<String> selection = new ArrayList<>();
    private String url;
    private static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip2);
        TextView bulletDate=findViewById(R.id.page2);
        bulletDate.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletTransport=findViewById(R.id.page3);
        bulletTransport.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletAccomodation=findViewById(R.id.page4);
        bulletAccomodation.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletActivity=findViewById(R.id.page5);
        bulletActivity.setTextColor(ContextCompat.getColor(this, R.color.colorPink));

        checkOutdoor = findViewById(R.id.checkOutdoor);
        checkWintersport = findViewById(R.id.checkWintersport);
        checkBeach = findViewById(R.id.checkBeach);
        checkSightseeing = findViewById(R.id.checkSightseeing);
        checkCulture = findViewById(R.id.checkCulture);
        checkNightlife = findViewById(R.id.checkNightlife);
        checkTraining = findViewById(R.id.checkTrainer);
        checkOther = findViewById(R.id.otherActivity);
        messageToUser = findViewById(R.id.messageToUser);


        Button btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                url = getIntent().getStringExtra(URL);
//                url+="&param6="+selection;
                url = getIntent().getStringExtra(URL) + "&param6=";
                for(String string : selection){
                    url += string + ",";
                }
                url = url.substring(0, url.length()-1);
                if (!checkOutdoor.isChecked() && !checkWintersport.isChecked()
                        && !checkBeach.isChecked() && !checkSightseeing.isChecked()
                        && !checkCulture.isChecked() && !checkNightlife.isChecked()
                        && !checkOther.isChecked() && !checkTraining.isChecked()){
                    messageToUser.setText("Välj något");
                } else {
                    Intent intent = new Intent(TripActivity.this, ChoosePackinglistName.class);
                    intent.putExtra(URL, url);
                    startActivity(intent);
                }
            }
        });
    }

    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.checkOutdoor:
                if(checked){
                    selection.add("outdoor");
                }else{
                    selection.remove("outdoor");
                }
                break;

            case R.id.checkWintersport:
                if(checked){
                    selection.add("skiing");
                }else{
                    selection.remove("skiing");
                }
                break;

            case R.id.checkBeach:
                if(checked){
                    selection.add("beach");
                }else{
                    selection.remove("beach");
                }
                break;

            case R.id.checkSightseeing:
                if(checked){
                    selection.add("sightseeing");
                }else{
                    selection.remove("sightseeing");
                }
                break;

            case R.id.checkCulture:
                if(checked){
                    selection.add("culture");
                }else{
                    selection.remove("culture");
                }
                break;

            case R.id.checkNightlife:
                if(checked){
                    selection.add("nightlife");
                }else{
                    selection.remove("nightlife");
                }
                break;

            case R.id.checkTrainer:
                if(checked){
                    selection.add("training");
                }else{
                    selection.remove("training");
                }
                break;

            case R.id.otherActivity:
                if(checked){
                    selection.add("other");
                }else{
                    selection.remove("other");
                }
                break;
        }
    }
}