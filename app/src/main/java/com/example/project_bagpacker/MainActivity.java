package com.example.project_bagpacker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity { //AppCompat

    private Button btnSkapaKonto;
    private Button btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogIn = (Button)findViewById(R.id.btnLogIn);

        btnSkapaKonto = (Button)findViewById(R.id.btnSkapaKonto);


        btnSkapaKonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Create_New_Account.class );
                startActivity(intent);
            }
        });



        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(MainActivity.this,Login.class );
                startActivity(intentLogin);
            }
        });


    }
}
