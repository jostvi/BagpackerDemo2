package se.mau.ai9856.bagpackerdemo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import java.util.ArrayList;

public class ShowSavedActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private ArrayList<SubList> expandableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expandableList = Database.loadList(this, "Min lista");
        setContentView(R.layout.activity_list_view);
        expandableListView = findViewById(R.id.expList);
        expandableListAdapter = new ExpandableListAdapter
                (ShowSavedActivity.this, expandableList);
        expandableListView.setAdapter(expandableListAdapter);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        expandAll();
    }

    private void expandAll(){
        int count = expandableListAdapter.getGroupCount();
        for(int i = 0; i < count; i++){
            expandableListView.expandGroup(i);
        }
    }
}
