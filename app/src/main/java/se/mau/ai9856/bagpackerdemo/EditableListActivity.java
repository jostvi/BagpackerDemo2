package se.mau.ai9856.bagpackerdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class EditableListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String ITEMS = "items";
    private static final String NAME = "name";
    private ExpandableListView expListView;
    private ArrayList<SubList> expList;
    private EditableListAdapter adapter;
    private EditText etNewItem;
    private String category;
    private String name;
    private boolean saved;

    @Override
    public void onBackPressed(){  // Skriv om så upprepningar i koden kan tas bort
        if (!saved){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditableListActivity.this);
            builder.setCancelable(true);
            builder.setMessage("Vill du spara listan?");
            builder.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    exitEditing();
                }
            });
            builder.setPositiveButton("Spara", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Database.saveName(EditableListActivity.this, "NAME", name);
                    Database.saveList(EditableListActivity.this, "LIST", expList);
                    exitEditing();
                }
            });
            builder.show();
        } else {
            exitEditing();
        }
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
    }

    private void initializeComponents() {
        Intent intent = getIntent();
        String list = intent.getStringExtra(ITEMS);
        name = intent.getStringExtra(NAME);
        Gson gson = new Gson();
        expList = gson.fromJson(list, new TypeToken<List<SubList>>(){}.getType());
        setContentView(R.layout.activity_edit_list);
        expListView = findViewById(R.id.expListView);
        etNewItem = findViewById(R.id.etNewItem);
        adapter = new EditableListAdapter(EditableListActivity.this, expList);
        expListView.setAdapter(adapter);
        setUpSpinner();
        expandAll();
        TextView title = findViewById(R.id.listTitle);
        saved = false;

        if(name == null){
            name = Database.loadName(this, "NAME");
            title.setText(name);
        } else
            title.setText(name);
    }

    private void exitEditing(){
        Intent intent = getIntent();
        if (intent.getStringExtra("ACTIVITY1") != null){
            intent = new Intent(this, ShowSavedListActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void addButtonClicked(View v) {
        String newItem = etNewItem.getText().toString().toLowerCase();
        boolean itemAdded = false;
        if (newItem.length() == 0 || category.isEmpty()) {
            etNewItem.setHint("Döp din grej!!!");
        } else {
            for (SubList subList : expList) {
                if (subList.getName().equals(category)) {
                    addItem(subList, newItem);
                    itemAdded = true;
                }
            }
            if (!itemAdded) {
                SubList subList = new SubList();
                subList.setName(category);
                addItem(subList, newItem);
                expList.add(subList);
                expListView.expandGroup(expList.indexOf(subList));
            }
        }
    }

    public void addItem(SubList subList, String newItem) {
        subList.addItem(new Packable(newItem, 1));
        adapter.notifyDataSetChanged();
        etNewItem.setText("");
        etNewItem.setHint("Lägg till föremål");
        Toast.makeText(this, "Du har lagt till: " + newItem, Toast.LENGTH_SHORT).show();
    }

    public void saveList(View v) {
        Database.saveName(this, "NAME", name);
        Database.saveList(this, "LIST", expList);
        saved = true;
        Toast.makeText(this, "\"" + name + "\"" + " sparad", Toast.LENGTH_SHORT).show();
    }

    private void expandAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expListView.expandGroup(i);
        }
    }

    public void setUpSpinner() {
        Spinner spinner = findViewById(R.id.spChooseCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        if (parent.getSelectedItemPosition() != 0) {
            category = (String) parent.getSelectedItem();
        } else {
            category = "";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}