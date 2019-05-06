package se.mau.ai9856.bagpackerdemo;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class DatePicker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private TextView dateFrom;
    private TextView dateTill;
    private Button btnFrom;
    private Button btnTill;
    private TextView showDateFrom;
    private TextView showDateTill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker2);
       /* btnFrom = findViewById(R.id.btnFrom);
        btnTill = findViewById(R.id.btnTill);*/
        showDateFrom = findViewById(R.id.showDateFrom);
        showDateTill = findViewById(R.id.showDateTill);


        findViewById(R.id.btnFrom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });

        findViewById(R.id.btnTill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });


    }
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        String dateFrom = dayOfMonth + "/" + month + "/" + year;
        showDateFrom.setText(dateFrom);
        //     showDateTill.setText(dateFrom);

    }


}