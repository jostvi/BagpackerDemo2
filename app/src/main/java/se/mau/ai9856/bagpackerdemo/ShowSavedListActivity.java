package se.mau.ai9856.bagpackerdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ShowSavedListActivity extends AppCompatActivity {
    private final static String ITEMS = "items";
    private ExpandableListView expandableListView;
    private SavedListAdapter savedListAdapter;
    private String listKey, nameKey, name;
    private Button btnLogOut;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        initializeComponents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowSavedListActivity.this, MainActivity.class);
                startActivity(intent);


            }
        });
    }

    private void initializeComponents() {
        listKey = "LIST";
        nameKey = "NAME";
        name = Database.loadName(this, nameKey);
        ArrayList<SubList> expandableList = Database.loadList(this, listKey);
        setContentView(R.layout.activity_show_saved);
        expandableListView = findViewById(R.id.SavedexpListView);
        TextView tv = findViewById(R.id.titleTextView);
        tv.setText(name);

        if (expandableList == null) {
            finish();
            Toast.makeText(this, "Du har inga sparade listor :(",
                    Toast.LENGTH_LONG).show();
        } else {
            savedListAdapter = new SavedListAdapter
                    (ShowSavedListActivity.this, expandableList);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowSavedListActivity.this);
        builder.setCancelable(true);
        builder.setMessage("Är du säker på att du vill radera \"" + name + "\"?");
        builder.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Radera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Database.deleteList(ShowSavedListActivity.this, listKey);
                Database.deleteName(ShowSavedListActivity.this, nameKey);
                finish();
            }
        });
        builder.show();
    }

    public void goToEditMode(View v) {
        Intent intent = new Intent(this, EditableListActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(Database.loadList(this, listKey));
        intent.putExtra(ITEMS, json);
        intent.putExtra("ACTIVITY1", "halloj");
        startActivity(intent);
    }
}
