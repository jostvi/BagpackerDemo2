package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class EditListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String ITEMS = "items";
    private ExpandableListView expListView;
    private ArrayList<SubList> expList;
    private EditableListAdapter adapter;
    private EditText etNewItem;
    private String category;

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
        String response = intent.getStringExtra(ITEMS);
        Gson gson = new Gson();
        expList = gson.fromJson(response, new TypeToken<List<SubList>>() {
        }.getType());
        setContentView(R.layout.activity_edit_list);
        expListView = findViewById(R.id.expListView);
        etNewItem = findViewById(R.id.etNewItem);
        adapter = new EditableListAdapter(EditListActivity.this, expList);
        expListView.setAdapter(adapter);
        setUpSpinner();
        expandAll();
    }

    public void addButtonClicked(View v) {
        String newItem = etNewItem.getText().toString();
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
        String key = "Min lista";
        Database.saveList(this, key, expList);
        Toast.makeText(this, "\"" + key + "\"" + " sparad", Toast.LENGTH_SHORT).show();
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