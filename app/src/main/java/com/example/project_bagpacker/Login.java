package com.example.project_bagpacker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
    private Button buttonLOGGAIN;
    private EditText ed1,ed2;
    private TextView textView;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    buttonLOGGAIN = (Button)findViewById(R.id.button);
    ed1 = (EditText)findViewById(R.id.editText);
    ed2 = (EditText)findViewById(R.id.editText2);


    textView = (TextView)findViewById(R.id.textView3);
      textView.setVisibility(View.GONE);

      buttonLOGGAIN.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(ed1.getText().toString().equals("admin") &&
                    ed2.getText().toString().equals("admin")) {
                Toast.makeText(getApplicationContext(),
                        "Redirecting...",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

                        textView.setVisibility(View.VISIBLE);
                textView.setBackgroundColor(Color.RED);
                counter--;
                textView.setText(Integer.toString(counter));

                if (counter == 0) {
                    buttonLOGGAIN.setEnabled(false);
                }
            }
        }
    });


}
}
