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

public class ListViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String ITEMS = "items";
    private ExpandableListView expListView;
    private ArrayList<SubList> expList;
    private ExpandableListAdapter expAdapter;
    private EditText etNewItem;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String response = intent.getStringExtra(ITEMS);
        Gson gson = new Gson();
        expList = gson.fromJson(response, new TypeToken<List<SubList>>(){}.getType());
        setContentView(R.layout.activity_list_view);
        expListView = findViewById(R.id.expListView);
        etNewItem = findViewById(R.id.etNewItem);
        expAdapter = new ExpandableListAdapter(ListViewActivity.this, expList);
        expListView.setAdapter(expAdapter);
        setUpSpinner();
        expandAll();
    }

    @Override
    public void onBackPressed() {
        Intent resetMain = new Intent(this, MainActivity.class); // skriv om! Nu startas en ny aktivitet varje gång
        finish();
        startActivity(resetMain);                      // ändra till NOHISTORY eller nåt
    }

    public void addButtonClicked(View v) {
        String newItem = etNewItem.getText().toString();
        boolean itemAdded = false;
        if (newItem.length() == 0 || category.isEmpty()) {
            etNewItem.setHint("Döp din grej!!!");
        } else {
            for(SubList subList : expList){
                if(subList.getName().equals(category)){
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

    public void addItem(SubList subList, String newItem){
        subList.addItem(new Packable(newItem));
        expAdapter.notifyDataSetChanged();
        etNewItem.setText("");
        etNewItem.setHint("Lägg till föremål");
        Toast.makeText(this, "Du har lagt till: " + newItem, Toast.LENGTH_SHORT).show();
    }

    public void saveList(View v) {
        String key = "Min lista";
        Database.saveList(this,key,expList);
        Toast.makeText(this,"\"" + key + "\"" + " sparad",Toast.LENGTH_SHORT).show();
}

    private void expandAll() {
        int count = expAdapter.getGroupCount();
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