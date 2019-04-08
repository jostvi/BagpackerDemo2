package se.mau.ai9856.bagpackerdemo;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
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
    protected void onListItemClick(ListView list, View view, int pos, long id){
        super.onListItemClick(list, view, pos, id);
        ListItem item = adapter.getItem(pos);
        Toast.makeText(this, "Hoppsan! Du tryckte på " + item.getItemName(), Toast.LENGTH_LONG).show();
    }
    private void setList(){
        stringList = getIntent().getStringArrayListExtra(ITEMS);
        for (int i = 0; i < stringList.size(); i++){
            ListItem item = new ListItem(i, stringList.get(i));
            list.add(item);
            Log.d("res", list.get(i).getItemName());
        }
        adapter = new CustomAdapter(this, list);
        setListAdapter(adapter);
    }
}
