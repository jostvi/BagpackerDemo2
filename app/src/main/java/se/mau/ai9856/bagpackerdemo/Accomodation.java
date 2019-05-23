package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class Accomodation extends AppCompatActivity {
    private CheckBox checkHotel;
    private CheckBox checkApartment;
    private CheckBox checkWithFriend;
    private CheckBox checkCaravan;
    private CheckBox checkTent;
    private CheckBox checkCottage;
    private CheckBox checkOther;
    private CheckBox checkHostel;
    private TextView messageToUser;
    private ArrayList<String> selection = new ArrayList<String>();
    private String url;
    private static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation2);
        TextView bulletDate = findViewById(R.id.page2);
        bulletDate.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletDestination = findViewById(R.id.page1);
        bulletDestination.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletTransport = findViewById(R.id.page3);
        bulletTransport.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletAccomodation = findViewById(R.id.page4);
        bulletAccomodation.setTextColor(ContextCompat.getColor(this, R.color.colorPink));

        checkHotel = findViewById(R.id.checkHotel);
        checkApartment = findViewById(R.id.checkApartment);
        checkWithFriend = findViewById(R.id.checkWithFriend);
        checkCaravan = findViewById(R.id.checkCaravan);
        checkTent = findViewById(R.id.checkTent);
        checkCottage = findViewById(R.id.checkCottage);
        checkOther = findViewById(R.id.otherAccommodation);
        checkHostel = findViewById(R.id.checkHostel);
        messageToUser = findViewById(R.id.messageToUser);

        Button btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                url = getIntent().getStringExtra(URL);
//                url+="&param5="+selection;
                url = getIntent().getStringExtra(URL) + "&param5=";
                for (String string : selection) {
                    url += string + ",";
                }
                url = url.substring(0, url.length() - 1);
                if (!checkHotel.isChecked() && !checkApartment.isChecked()
                        && !checkWithFriend.isChecked() && !checkCaravan.isChecked()
                        && !checkTent.isChecked() && !checkCottage.isChecked()
                        && !checkOther.isChecked() && !checkHostel.isChecked()) {
                    messageToUser.setText("Välj något");
                } else {
                    Intent intent = new Intent(Accomodation.this, TripActivity.class);
                    intent.putExtra(URL, url);
                    startActivity(intent);
                }

            }
        });
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checkHotel:
                if (checked) {
                    selection.add("hotel");
                } else {
                    selection.remove("hotel");
                }
                break;

            case R.id.checkApartment:
                if (checked) {
                    selection.add("apartment");
                } else {
                    selection.remove("apartment");
                }
                break;

            case R.id.checkWithFriend:
                if (checked) {
                    selection.add("friends");
                } else {
                    selection.remove("friends");
                }
                break;

            case R.id.checkCaravan:
                if (checked) {
                    selection.add("camper");
                } else {
                    selection.remove("camper");
                }
                break;

            case R.id.checkBike:
                if (checked) {
                    selection.add("bike");
                } else {
                    selection.remove("bike");
                }
                break;

            case R.id.checkTent:
                if (checked) {
                    selection.add("tent");
                } else {
                    selection.remove("tent");
                }
                break;

            case R.id.checkCottage:
                if (checked) {
                    selection.add("cottage");
                } else {
                    selection.remove("cottage");
                }
                break;

            case R.id.checkHostel:
                if (checked) {
                    selection.add("hostel");
                } else {
                    selection.remove("hostel");
                }
                break;

            case R.id.otherAccommodation:
                if (checked) {
                    selection.add("other");
                } else {
                    selection.remove("other");
                }
                break;
        }
    }
}