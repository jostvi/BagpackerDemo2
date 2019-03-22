package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText inputTxt;
    private String[] items = {"Badkläder","Strumpor","T-Shirt","Gummistövlar","Tandborste",
            "Chokladkartong","Lusekofta","Pyjamas","Pass","Astmaspray","Toalettborste",
            "Badanka","Laddare","Sladdar","Midjeväska","Fotboll","Tennisboll","Badboll",
            "Underbyxor","Monokel","Damasker","Bajspåsar","Sprit","Knark" };
    private static final String ITEMS = "items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputTxt = findViewById(R.id.inputTxt);
    }

    public void showList(View getListBtn){
        Intent showListIntent = new Intent(this, Activity2.class);
        String destination = inputTxt.getText().toString();

        if(destination.equalsIgnoreCase("Gävle")) {
            String[] toGefle = {items[1],items[3],items[21],items[22]};
            showListIntent.putExtra(ITEMS, toGefle);
            startActivity(showListIntent);
        }
        else if(destination.equalsIgnoreCase("Töreboda")) {
            showListIntent.putExtra(ITEMS, items);
        }
        else{
            String[] toAnywhere = {"Handduk"};
            showListIntent.putExtra(ITEMS, toAnywhere);
        }
        startActivity(showListIntent);
    }
}
