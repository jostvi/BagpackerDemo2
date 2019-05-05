package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * This is where the user can log in, username and password are allready saved locally.
 *
 * @author Ekaterina K
 */

public class Login extends AppCompatActivity {
    private EditText loginUsername;
    private EditText loginPassword;
    private Button buttonLogin;
    SharedPreferences sharedPreferences;



    private static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = (EditText)findViewById(R.id.loginUsername);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);

        sharedPreferences = getSharedPreferences(CreateNewAccount.mypreference, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(CreateNewAccount.mypreference, Context.MODE_PRIVATE);


        if(sharedPreferences.contains(CreateNewAccount.Name)){
            loginUsername.setText(sharedPreferences.getString(CreateNewAccount.Name, ""));
        }
        if(sharedPreferences.contains(CreateNewAccount.LÖSENORD)){
            loginPassword.setText(sharedPreferences.getString(CreateNewAccount.LÖSENORD, ""));
        }
        buttonLogin.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Login.this, SavedLists.class);
                startActivity(intent);


            }
        });

    }



}
