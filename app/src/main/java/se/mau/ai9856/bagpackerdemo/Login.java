package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private EditText loginUsername;
    private EditText loginPassword;
    private Button buttonLogin;
    SharedPreferences sharedPreferences;
    static final String mypreference = "mypref";
    static final String Name = "nameKey";
    static final String LÖSENORD = "emailKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = (EditText)findViewById(R.id.loginUsername);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);

        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);


        if(sharedPreferences.contains(Name)){
            loginUsername.setText(sharedPreferences.getString(Name, ""));
        }
        if(sharedPreferences.contains(LÖSENORD)){
            loginPassword.setText(sharedPreferences.getString(LÖSENORD, ""));
        }
        buttonLogin.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Login.this, ShowSavedActivity.class);
                startActivity(intent);


            }
        });

    }



}
