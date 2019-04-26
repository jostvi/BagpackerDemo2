package se.mau.ai9856.bagpackerdemo;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

public class SavedLists extends AppCompatActivity {
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_lists);
        list = Database.loadList(this, "SPARAD");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_layout,
                                R.id.itemName, list);
        ListView savedListsList = findViewById(R.id.savedListsList);
        savedListsList.setAdapter(adapter);
    }
}
