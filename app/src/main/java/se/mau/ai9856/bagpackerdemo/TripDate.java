package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TripDate extends AppCompatActivity {
    private List<Date> listDate;
    private CalendarPickerView datePicker;
    private String url;
    private static final String URL = "url";
    private static String simpleStartDate, simpleEndDate;
    private static final String STARTDATESAVE = "startDateSave";
    private static final String STOPDATESAVE = "stopDateSave";
   /* private Date startDate;
    private SimpleDateFormat sdf;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_date2);
        TextView bulletDate=findViewById(R.id.page2);
        bulletDate.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletDestination=findViewById(R.id.page1);
        bulletDestination.setTextColor(ContextCompat.getColor(this, R.color.colorPink));

        if (savedInstanceState != null) {
            simpleStartDate = savedInstanceState.getString(STARTDATESAVE);
            simpleEndDate = savedInstanceState.getString(STOPDATESAVE);

        }


        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);

        final Button btnOk = findViewById(R.id.btnNext);
        btnOk.setEnabled(false);
        btnOk.setTextColor(ContextCompat.getColor(this, R.color.colorInputField));
        datePicker = findViewById(R.id.datePicker);

        datePicker.init(today, calendar.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);


        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                // String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);
                btnOk.setEnabled(false);
                btnOk.setTextColor(ContextCompat.getColor(TripDate.this,
                        R.color.colorInputField));
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



                url = getIntent().getStringExtra(URL);
                Date startDate = listDate.get(0);
                Date stopDate = listDate.get(listDate.size()-1);
                simpleStartDate = sdf.format(startDate);
                simpleEndDate = sdf.format(stopDate);

                } if ( !(simpleStartDate.equals(simpleEndDate)) ) {
                    btnOk.setEnabled(true);
                    btnOk.setTextColor(ContextCompat.getColor(TripDate.this, R.color.colorYellow));
                }

                url +="&param2=20" + simpleStartDate + "&param3=20" + simpleEndDate;
            }


            @Override
            public void onDateUnselected(Date date) {

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String message = "Välj avrese- och returresedatum";
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

    /*@Override
    protected void onResume() {
        super.onResume();
        try {
            startDate = sdf.parse(simpleStartDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // destination.setText(dest);
    }
*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STARTDATESAVE, simpleStartDate);
     //   outState.putString(STOPDATESAVE, simpleEndDate);

    }


    public static String getStartDate(){
        return simpleStartDate;
    }

    public static String getEndDate(){
        return simpleEndDate;
    }

}