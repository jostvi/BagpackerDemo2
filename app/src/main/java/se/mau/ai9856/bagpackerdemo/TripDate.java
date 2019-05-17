package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class TripDate extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private List<Date> listDate;
    private CalendarPickerView datePicker;
    private Button btnOk;
    private ImageButton arrowBack;
    private String url;
    private static final String URL = "url";
    private static final String SHARED_PREF = "sharedPref";
    private static final String STARTDATE_TOSAVE = "startDate";
    private static final String STOPDATE_TOSAVE = "stopDate";
    private String startDateToPrint;
    private String stopDateToPrint;
    private DataManager dataManager;
    private String simpleStartDate;
    private String simpleStoptDate;

    // private String startDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_date2);
       /* Date convert = null;
        if (dataManager.getStartData() != "" && dataManager.getStopData() != ""){
            SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
            try {
                convert = sdf.parse(dataManager.getStartData());
            } catch (ParseException e){
                e.printStackTrace();
            }

            listDate.set(0, convert );


        }*/


        //String url = getIntent().getStringExtra(URL);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);

        btnOk = findViewById(R.id.btnOk);
        arrowBack = findViewById(R.id.arrowBack);
        datePicker = findViewById(R.id.datePicker);

        datePicker.init(today, calendar.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);


        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                // String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);

               /* String startDatum = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                           + " " + (calSelected.get(Calendar.MONTH) + 1)
                            + " " + calSelected.get(Calendar.YEAR);
                    Toast.makeText(TripDate.this, startDatum, Toast.LENGTH_SHORT).show();
                String stopDatum = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + " " + (calSelected.get(Calendar.MONTH) + 1)
                        + " " + calSelected.get(Calendar.YEAR);
                Toast.makeText(TripDate.this, stopDatum, Toast.LENGTH_SHORT).show();*/

                /*String selectedDate = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + " " + (calSelected.get(Calendar.MONTH) + 1)
                        + " " + calSelected.get(Calendar.YEAR);
                Toast.makeText(TripDate.this, selectedDate, Toast.LENGTH_SHORT).show();*/
                datePicker.getSelectedDates();
                listDate = datePicker.getSelectedDates();

                SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd"); //---------
                for(int i=0; i<listDate.size(); i++){
                    Date tempDate = listDate.get(i);
                    String formattedDate = sdf.format(tempDate);
                }

                url = getIntent().getStringExtra(URL);
                Date startDate = listDate.get(0);
                Date stopDate = listDate.get(listDate.size()-1);
                simpleStartDate = sdf.format(startDate);
                simpleStoptDate = sdf.format(stopDate);

                url +="&param2=20" + simpleStartDate + "&param3=20" + simpleStoptDate;//tripDate
                Log.e("hej ", "url" + url);





            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });



        //    Log.e("hej ", "url" + url);
        //   url = getIntent().getStringExtra(URL);

        //   String startDate = listDate.get(0).toString();
        //  String stopDate = listDate.get(listDate.size()-1).toString();


        arrowBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDate.this, Destination.class);
                startActivity(intent);

            }
        });




        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
             /*  dataManager.setStartData(simpleStartDate);
               dataManager.setStopData(simpleStoptDate);*/
                String message = "Välj datum";
                if (datePicker.getSelectedDates().size() == 0 || datePicker.getSelectedDates().size() == 1) {
                    Toast.makeText(TripDate.this, message, Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(TripDate.this, Transport.class);
                    intent.putExtra(URL, url);
                    startActivity(intent);
                }

            }
        });
    }

  /*  public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STARTDATE_TOSAVE, String.valueOf(listDate.get(0)));
        editor.putString(STOPDATE_TOSAVE, String.valueOf(listDate.size()-1));
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        startDateToPrint = sharedPreferences.getString(STARTDATE_TOSAVE, "");
        stopDateToPrint = sharedPreferences.getString(STOPDATE_TOSAVE, "");
    }

    public void updateViews(){


    }
*/
}