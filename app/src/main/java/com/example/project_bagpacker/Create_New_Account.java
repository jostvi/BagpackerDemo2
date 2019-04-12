package com.example.project_bagpacker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Create_New_Account extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button btnLogin;

    SharedPreferences sharedPreferences;
    static final String mypreference = "mypref";
    static final String Name = "nameKey";
    static final String LÖSENORD = "emailKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__new__account);

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

                validate(username.getText().toString(), password.getText().toString());
            }
        });
    }

    public void validate(String username, String password){
        if (username.equals("Bagpacker ") && password.equals("A") ){
            Intent intent = new Intent (Create_New_Account.this, Packing_List.class);
            startActivity(intent);
        }
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
   /* public void validate(String username, String password){
        if(username.equals(Name) && password.equals(LÖSENORD)){
            Intent intent = new Intent (Create_New_Account.this, Packing_List.class);
            startActivity(intent);
        }

    }*/
}

/*
package com.example.project_bagpacker;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;

public class Create_New_Account extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__new__account);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(username.getText().toString(), password.getText().toString());
            }
        });
    }

    public void validate(String username, String password){
        if (username.equals("Bagpacker ") && password.equals("A") ){
            Intent intent = new Intent (Create_New_Account.this, Packing_List.class);
            startActivity(intent);
        }
    }
}

*/
