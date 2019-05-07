package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class TripDate extends AppCompatActivity {
    private List<Date> listDate;
    private CalendarPickerView datePicker;
    private Button btnOk;
    private String url;
    private static final String URL = "url";
    // private String startDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_date2);


        //String url = getIntent().getStringExtra(URL);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);

        btnOk = findViewById(R.id.btnOk);
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

                String selectedDate = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + " " + (calSelected.get(Calendar.MONTH) + 1)
                        + " " + calSelected.get(Calendar.YEAR);
                Toast.makeText(TripDate.this, selectedDate, Toast.LENGTH_SHORT).show();
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
                String simpleStartDate = sdf.format(startDate);
                String simpleStoptDate = sdf.format(stopDate);

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




        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
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

}