package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import static android.view.View.VISIBLE;

public class PackListActivity extends AppCompatActivity {
    private String[] items;
    private Button saveButton;
    private Button deleteButton;
    private CheckBox itemCheck;
    private TextView itemName;
    private static final String ITEM = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initializeComponents();
        addItem();
    }

    private void initializeComponents(){
       // for (String name : items){
           // new Item(name);
        //}
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);
        itemCheck = findViewById(R.id.itemCheck);
        itemName = findViewById(R.id.itemName);
    }

    private void addItem(){

        String item = getIntent().getStringExtra(ITEM);
        if(!item.equals("") || !item.equals("Skriv nåt här")){
            itemName.setText(item);
            itemCheck.setVisibility(VISIBLE);
            itemName.setVisibility(VISIBLE);
            deleteButton.setVisibility(VISIBLE);
        }

    }

    public static class Item extends Fragment {
        private TextView itemName;
        private CheckBox itemCheck;
        private ViewGroup itemPanel;

        public Item() {

            // itemPanel = (ViewGroup) findViewById(R.id.itemCheck);
        }
    }
}
