package se.mau.ai9856.bagpackerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowSavedActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String key = "Min lista";
        ArrayList<SubList> expandableList = Database.loadList(this, key);
        setContentView(R.layout.activity_show_saved);
        expandableListView = findViewById(R.id.SavedexpListView);
        TextView tv = findViewById(R.id.titleTextView);
        tv.setText(key);

        if (expandableList == null){
            expandableList = new ArrayList<>();
        } else {
            expandableListAdapter = new ExpandableListAdapter
                    (ShowSavedActivity.this, expandableList);
            expandableListView.setAdapter(expandableListAdapter);
            expandAll();
        }
    }

    private void expandAll(){
        int count = expandableListAdapter.getGroupCount();
        for(int i = 0; i < count; i++){
            expandableListView.expandGroup(i);
        }
    }
}
