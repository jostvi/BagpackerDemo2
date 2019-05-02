package se.mau.ai9856.bagpackerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import java.util.ArrayList;

public class ShowSavedActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<SubList> expandableList = Database.loadList(this, "Min lista");
        setContentView(R.layout.activity_list_view);
        expandableListView = findViewById(R.id.expList);
        EditText et = findViewById(R.id.etNewItem);

        if (expandableList == null){
            expandableList = new ArrayList<>();
            et.setHint("Du har inga sparade listor :(");
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
