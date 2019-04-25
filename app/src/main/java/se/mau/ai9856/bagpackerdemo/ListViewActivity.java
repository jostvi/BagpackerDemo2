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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class ListViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String ITEMS = "items";
    private ExpandableListView expListView;
    private LinkedHashMap<String, Header> categorySubList = new LinkedHashMap<>();
    private ArrayList<Header> categories = new ArrayList<>();
    private ArrayList<Packable> list = new ArrayList<>();
    private ExpandableListAdapter expAdapter;
    private EditText etNewItem;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        expListView = findViewById(R.id.expList);
        etNewItem = findViewById(R.id.etNewItem);
        expAdapter = new ExpandableListAdapter(ListViewActivity.this, categories);
        expListView.setAdapter(expAdapter);
        setExpandableList();
        setUpSpinner();
        expandAll();
    }

    @Override
    public void onBackPressed() {
        Intent resetMain = new Intent(this, MainActivity.class);
        saveList();
        finish();
        startActivity(resetMain);
    }

    public void addButtonClicked(View v) {
        String newItem = etNewItem.getText().toString();

        if (newItem.length() == 0 || category.isEmpty()) {
            etNewItem.setHint("Du måste döpa din grej!!!");
        } else {
            Header header = categorySubList.get(category);
            ArrayList<Packable> list = header.getItemList();
            list.add(new Packable(newItem));
            sortList(list);
            header.setItemList(list);
            expAdapter.notifyDataSetChanged();
            etNewItem.setText("");
            Toast.makeText(this, "Du lade till: " + newItem, Toast.LENGTH_SHORT).show();
        }
    }

    public void saveList() {
        ArrayList<String> stringList = new ArrayList<>();
        for (Packable item : list) {
            stringList.add(item.getItemName());
        }
        Database.saveList(this, "SPARAD", stringList);
        Toast.makeText(this, "Lista sparad", Toast.LENGTH_LONG).show();
    }

    public void setExpandableList() {
        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra(ITEMS));
            JSONArray jsonArray = jsonObject.getJSONArray("lista");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.getJSONObject(i);
                String category = jObject.getString("category");
                Header header = categorySubList.get(category);

                if (header == null) {
                    header = new Header();
                    header.setName(category);
                    categorySubList.put(category, header);
                    categories.add(header);
                }
                
                ArrayList<Packable> itemList = header.getItemList();
                Packable packable = new Packable(jObject.getString("item"));
                itemList.add(packable);
                sortList(itemList);
                header.setItemList(itemList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void expandAll() {
        int count = expAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expListView.expandGroup(i);
        }
    }

    public void sortList(ArrayList<Packable> list) {
        Collections.sort(list, new Comparator<Packable>() {
            @Override
            public int compare(Packable o1, Packable o2) {
                return o1.getItemName().compareTo(o2.getItemName());
            }
        });
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