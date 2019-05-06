package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class Transport extends AppCompatActivity {
    private TextView questionTransport;
    private CheckBox checkCar;
    private CheckBox checkTrain;
    private CheckBox checkFlight;
    private CheckBox checkBus;
    private CheckBox checkBike;
    private CheckBox checkMotorbike;
    private CheckBox checkBoat;
    private Button btnOk;
    private TextView messageToUser;
    private String url;
    private static final String URL = "url";
    private ArrayList<String> selection = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport2);

        questionTransport = findViewById(R.id.questionTransport);
        checkCar = findViewById(R.id.checkCar);
        checkTrain = findViewById(R.id.checkTrain);
        checkFlight = findViewById(R.id.checkFlight);
        checkBus = findViewById(R.id.checkBus);
        checkBike = findViewById(R.id.checkBike);
        checkMotorbike = findViewById(R.id.checkMotorbike);
        checkBoat = findViewById(R.id.checkBoat);
        btnOk = findViewById(R.id.btnOk);
        messageToUser = findViewById(R.id.messageToUser);




        btnOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                url = getIntent().getStringExtra(URL);
                url+="&param4="+selection;
                Log.e("hej ", "url" + url);
                //   String url = "http://bagpacker.pythonanywhere.com/android/?param1=" + destination.getText().toString();

                if(!checkCar.isChecked() && !checkTrain.isChecked() && !checkFlight.isChecked()
                        && !checkBus.isChecked() && !checkBike.isChecked() && !checkBoat.isChecked()
                        && !checkMotorbike.isChecked()) {
                    messageToUser.setText("Välj något");

                }else {
                    Intent intent = new Intent(Transport.this, Accomodation.class);
                    intent.putExtra(URL, url);
                    startActivity(intent);
                }
            }
        });

    }



    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.checkCar:
                if(checked){
                    selection.add("Bil");
                }else{
                    selection.remove("Bil");
                }
                break;

            case R.id.checkTrain:
                if(checked){
                    selection.add("Tåg");
                }else{
                    selection.remove("Tåg");
                }
                break;

            case R.id.checkFlight:
                if(checked){
                    selection.add("Flyg");
                }else{
                    selection.remove("Flyg");
                }
                break;

            case R.id.checkBus:
                if(checked){
                    selection.add("Buss");
                }else{
                    selection.remove("Buss");
                }
                break;

            case R.id.checkBike:
                if(checked){
                    selection.add("Cykel");
                }else{
                    selection.remove("Cykel");
                }
                break;

            case R.id.checkMotorbike:
                if(checked){
                    selection.add("Motoykel");
                }else{
                    selection.remove("Motocykel");
                }
                break;

            case R.id.checkBoat:
                if(checked){
                    selection.add("Båt");
                }else{
                    selection.remove("Båt");
                }
                break;




        }

        /*url = getIntent().getStringExtra(URL);
        url+="&param3="+selection;
        Log.e("hej ", "url" + url);*/

    }


}