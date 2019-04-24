package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class ListViewActivity extends AppCompatActivity {
    private static final String ITEMS = "items";
    private ExpandableListView expListView;
    private LinkedHashMap<String, HeaderInfo> mySection = new LinkedHashMap<>();
    private ArrayList<HeaderInfo> sectionList = new ArrayList<>();
    private ArrayList<ListItem> list = new ArrayList<>();
    private ExpandableListAdapter expAdapter;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
       // newItem = findViewById(R.id.add_item);
        //deleteItem = findViewById(R.id.delete_item);
        //listView = findViewById(R.id.listView);
        //setList();

        // test av exp. list:

        expListView = findViewById(R.id.expList);
        expAdapter = new ExpandableListAdapter(ListViewActivity.this, sectionList);
        expListView.setAdapter(expAdapter);
        setExpandableList();
        expandAll();
    }

    @Override
    public void onBackPressed() {
        Intent resetMain = new Intent(this, MainActivity.class);
        saveList();
        finish();
        startActivity(resetMain);
    }

    public void addNewItem(View v) {
        /*String name;
        if (!newItem.getText().toString().isEmpty()) {
            name = newItem.getText().toString();
            list.add(new ListItem(list.size() - 1, name));

            Collections.sort(list, new Comparator<ListItem>() {
                @Override
                public int compare(ListItem o1, ListItem o2) {
                    return o1.getItemName().compareTo(o2.getItemName());
                }
            });
            list.trimToSize();
            refreshList(newItem);
            newItem.setText(null);
            Toast.makeText(ListViewActivity.this, "Du lade till: " + name, Toast.LENGTH_LONG).show();
        }*/
    }

    public void saveList(){
        ArrayList<String> stringList = new ArrayList<>();
        for (ListItem item : list){
            stringList.add(item.getItemName());
        }
        Database.saveList(this, "SPARAD", stringList);
        Toast.makeText(this, "Lista sparad", Toast.LENGTH_LONG).show();
    }

   /* private void setList() {
        ArrayList<String> stringList = getIntent().getStringArrayListExtra(ITEMS);

        for (int i = 0; i < stringList.size(); i++) {
            ListItem item = new ListItem(i, stringList.get(i));
            list.add(item);
        }
        adapter = new CustomAdapter(this, list);
        //listView.setAdapter(adapter);
    }*/

    // test exp. list:

    public void setExpandableList(){
        try{
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra(ITEMS));
            jsonArray = jsonObject.getJSONArray("lista");
        } catch (JSONException e){
            e.printStackTrace();
        }
        addItem(jsonArray);
    }

    private void expandAll(){
        int count = expAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expListView.expandGroup(i);
        }
    }

    private void collapseAll(){
        int count = expAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expListView.collapseGroup(i);
        }
    }

    public void addItem(JSONArray jArray) {

        try{
            for (int i = 0; i < jArray.length(); i++){
                JSONObject jObject = jArray.getJSONObject(i);
                String category = jObject.getString("category");
                HeaderInfo headerInfo = mySection.get(category);
                if (headerInfo == null) {
                    headerInfo = new HeaderInfo();
                    headerInfo.setName(category);
                    mySection.put(category, headerInfo);
                    sectionList.add(headerInfo);
                }
                ArrayList<ListItem> itemList = headerInfo.getItemList();
                int listSize = itemList.size();
                listSize++;

                ListItem listItem = new ListItem(listSize, jObject.getString("item"));
                itemList.add(listItem);
                headerInfo.setItemList(itemList);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private class AddItemRow {
        private EditText input = findViewById(R.id.new_item);
    }
}
