package se.mau.ai9856.bagpackerdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class Activity2 extends ListActivity {
    private static final String ITEMS = "items";
    private CustomAdapter adapter;
    private ArrayList<ListItem> list = new ArrayList<>();
    private ArrayList<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);
        setList();
    }

    @Override
    public void onBackPressed(){
        Intent resetMain = new Intent(this, MainActivity.class);
        finish();
        startActivity(resetMain);
    }

    private void setList(){
        stringList = getIntent().getStringArrayListExtra(ITEMS);
        for (int i = 0; i < stringList.size(); i++){
            ListItem item = new ListItem(i, stringList.get(i));
            list.add(item);
        }
        adapter = new CustomAdapter(this, list);
        setListAdapter(adapter);
    }
}
