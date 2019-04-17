package se.mau.ai9856.bagpackerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListViewActivity extends AppCompatActivity {
    private static final String ITEMS = "items";
    private CustomAdapter adapter;
    private ArrayList<ListItem> list;
    private ListView listView;
    private EditText newItem;
    private Button deleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        list = new ArrayList<>();
        newItem = findViewById(R.id.new_item);
        deleteItem = findViewById(R.id.delete_item);
        listView = findViewById(R.id.listView);
        setList();
    }

    @Override
    public void onBackPressed() {
        Intent resetMain = new Intent(this, MainActivity.class);
        finish();
        startActivity(resetMain);
    }

    public void saveNewItem(View v) {
        String name;
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
        }
    }

    public void deleteItem(View v){
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).isSelected){
                list.remove(i);
            }
        }
        refreshList(deleteItem);
    }

    public void refreshList(View v) {
        adapter.notifyDataSetChanged();
        listView.invalidateViews();
        listView.refreshDrawableState();
    }

    public void saveList(View v){
        ArrayList<String> stringList = new ArrayList<>();
        for (ListItem item : list){
            stringList.add(item.getItemName());
        }
        Database.saveList(this, "SPARAD", stringList);
        Toast.makeText(this, "Lista sparad", Toast.LENGTH_LONG).show();
    }

    private void setList() {
        ArrayList<String> stringList = getIntent().getStringArrayListExtra(ITEMS);
        //View header = getLayoutInflater().inflate(R.layout.header_layout, null);
        //TextView headerText = findViewById(R.id.list_header);
        //headerText.setText(stringList.get(0));
        //listView.addHeaderView(header);
        for (int i = 0; i < stringList.size(); i++) {
            ListItem item = new ListItem(i, stringList.get(i));
            list.add(item);
        }
        adapter = new CustomAdapter(this, list);
        listView.setAdapter(adapter);
    }
}
