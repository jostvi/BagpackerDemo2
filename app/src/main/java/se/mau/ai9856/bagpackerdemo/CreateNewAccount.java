package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * This is where the user can create a new account. The username and password is stored locally.
 * From here the user can go to CreateTripActivity
 *
 * @author Ekaterina K
 */
public class CreateNewAccount extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button btnLogin;

    SharedPreferences sharedPreferences;
    static final String mypreference = "mypreference";
    static final String Name = "nameKey";
    static final String LÖSENORD = "lösenordKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        if(sharedPreferences.contains(Name)){
            username.setText(sharedPreferences.getString(Name, ""));
        }
        if(sharedPreferences.contains(LÖSENORD)){
            password.setText(sharedPreferences.getString(LÖSENORD, ""));
        }
        btnLogin.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (CreateNewAccount.this, CreateTripActivity.class);
                startActivity(intent);


            }
        });
    }



    public void save(View v){
        String name = username.getText().toString();
        String passw = password.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Name, name);
        editor.putString(LÖSENORD, passw);
        editor.commit();
        retrive(v);
    }

    public void retrive(View v){
        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(Name)){
            username.setText(sharedPreferences.getString(Name, ""));
        }
        if(sharedPreferences.contains(LÖSENORD)){
            password.setText(sharedPreferences.getString(LÖSENORD, ""));
        }
    }
}
