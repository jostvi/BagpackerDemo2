package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.squareup.timessquare.CalendarPickerView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Class TripDate where the the user can choose trip's start date and stop date in a calendar.
 * The class is sending the user paramethers as a string to the Transport class.
 */

public class TripDate extends AppCompatActivity {
    private List<Date> listDate;
    private CalendarPickerView datePicker;
    private String url;
    private static final String URL = "url";
    private static String simpleStartDate, simpleEndDate;
    private static final String STARTDATESAVE = "startDateSave";
    private static final String STOPDATESAVE = "stopDateSave";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_date2);
        TextView bulletDate = findViewById(R.id.page2);
        bulletDate.setTextColor(ContextCompat.getColor(this, R.color.colorPink));
        TextView bulletDestination = findViewById(R.id.page1);
        bulletDestination.setTextColor(ContextCompat.getColor(this, R.color.colorPink));

        if (savedInstanceState != null) {
            simpleStartDate = savedInstanceState.getString(STARTDATESAVE);
            simpleEndDate = savedInstanceState.getString(STOPDATESAVE);
        }

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);

        final Button btnNext = findViewById(R.id.btnNext);
        btnNext.setEnabled(false);
        btnNext.setTextColor(ContextCompat.getColor(this, R.color.colorInputField));
        datePicker = findViewById(R.id.datePicker);

        datePicker.init(today, calendar.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);


        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);
                btnNext.setEnabled(false);
                btnNext.setTextColor(ContextCompat.getColor(TripDate.this,
                        R.color.colorInputField));
                datePicker.getSelectedDates();
                listDate = datePicker.getSelectedDates();

                SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
                url = getIntent().getStringExtra(URL);

                for (int i = 0; i < listDate.size(); i++) {

                    Date startDate = listDate.get(0);
                    Date stopDate = listDate.get(listDate.size() - 1);
                    simpleStartDate = sdf.format(startDate);
                    simpleEndDate = sdf.format(stopDate);
                }

                if (!(simpleStartDate.equals(simpleEndDate))) {
                    btnNext.setEnabled(true);
                    btnNext.setTextColor(ContextCompat.getColor(TripDate.this, R.color.colorYellow));
                }

                url += "&param2=20" + simpleStartDate + "&param3=20" + simpleEndDate;
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDate.this, Transport.class);
                intent.putExtra(URL, url);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STARTDATESAVE, simpleStartDate);
    }

    public static String getStartDate() {
        return simpleStartDate;
    }

    public static String getEndDate() {
        return simpleEndDate;
    }
}