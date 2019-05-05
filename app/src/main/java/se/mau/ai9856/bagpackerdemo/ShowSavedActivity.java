package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ShowSavedActivity extends AppCompatActivity {
    private final static String ITEMS = "items";
    private ExpandableListView expandableListView;
    private SavedListAdapter savedListAdapter;
    private String key;

    @Override
    public void onRestart() {
        super.onRestart();
        initializeComponents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
    }

    private void initializeComponents() {
        key = "Min lista";
        ArrayList<SubList> expandableList = Database.loadList(this, key);
        setContentView(R.layout.activity_show_saved);
        expandableListView = findViewById(R.id.SavedexpListView);
        TextView tv = findViewById(R.id.titleTextView);
        tv.setText(key);

        if (expandableList == null) {
            finish();
            Toast.makeText(this, "Du har inga sparade listor :(",
                    Toast.LENGTH_LONG).show();
        } else {
            savedListAdapter = new SavedListAdapter
                    (ShowSavedActivity.this, expandableList);
            expandableListView.setAdapter(savedListAdapter);
            expandAll();
        }
    }

    private void expandAll() {
        int count = savedListAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.expandGroup(i);
        }
    }

    public void deleteList(View v) {
        Database.deleteList(this, key);
        finish();
        Toast.makeText(this, "Du har tagit bort " + "\"" + key + "\"",
                Toast.LENGTH_LONG).show();
    }

    public void goToEditMode(View v) {
        Intent intent = new Intent(this, EditListActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(Database.loadList(this, key));
        intent.putExtra(ITEMS, json);
        startActivity(intent);
    }
}
