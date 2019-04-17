package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartPage extends AppCompatActivity {
    private Button btnCreateList;
    private Button btnLogIn;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        btnLogIn = (Button)findViewById(R.id.btnLogIn);
        btnCreateAccount = (Button)findViewById(R.id.btnCreateAccount);
        btnCreateList = (Button)findViewById(R.id.btnCreateList);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPage.this,CreateNewAccount.class );
                startActivity(intent);

            }
        });
    }
}
