package com.example.project_bagpacker;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Packing_List2 extends ListActivity {
    private static final String ITEMS = "items";
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing__list3);
        setList();

        /** Reference to the add button of the layout main.xml */
        Button btn = (Button) findViewById(R.id.btnAdd);

        /** Reference to the delete button of the layout main.xml */
        Button btnDel = (Button) findViewById(R.id.btnDel);

        /** Defining a click event listener for the button "Add" */
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText) findViewById(R.id.textItem);
                adapter.add(edit.getText().toString());

                edit.setText("");
                adapter.notifyDataSetChanged();

            }
        };

        /** Setting the event listener for the add button */
        btn.setOnClickListener(listener);

        /** Setting the event listener for the delete button */
    //    btnDel.setOnClickListener(listenerDel);

        /** Setting the adapter to the ListView */
        setListAdapter(adapter);

    }
    private void setList(){
        ArrayList<String> stuff = getIntent().getStringArrayListExtra(ITEMS);
        adapter = new ArrayAdapter<>(this, R.layout.row_layout, R.id.itemName, stuff);
        setListAdapter(adapter);
    }
}

/*
package com.example.project_bagpacker;

        import android.app.ListActivity;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.SparseBooleanArray;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;

public class Packing_List2 extends ListActivity {
    private static final String ITEMS = "items";
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing__list3);
        setList();

        */
/** Reference to the add button of the layout main.xml *//*

        Button btn = (Button) findViewById(R.id.btnAdd);

        */
/** Reference to the delete button of the layout main.xml *//*

        Button btnDel = (Button) findViewById(R.id.btnDel);

        */
/** Defining a click event listener for the button "Add" *//*

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText) findViewById(R.id.textItem);
                adapter.add(edit.getText().toString());

                edit.setText("");
                adapter.notifyDataSetChanged();

            }
        };

        */
/** Setting the event listener for the add button *//*

        btn.setOnClickListener(listener);

        */
/** Setting the event listener for the delete button *//*

        //    btnDel.setOnClickListener(listenerDel);

        */
/** Setting the adapter to the ListView *//*

        setListAdapter(adapter);

    }
    private void setList(){
        ArrayList<String> stuff = getIntent().getStringArrayListExtra(ITEMS);
        adapter = new ArrayAdapter<>(this, R.layout.row_layout, R.id.itemName, stuff);
        setListAdapter(adapter);
    }
}

*/
