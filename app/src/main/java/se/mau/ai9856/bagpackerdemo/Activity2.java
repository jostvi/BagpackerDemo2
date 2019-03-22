package se.mau.ai9856.bagpackerdemo;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Activity2 extends ListActivity {
    private static final String ITEMS = "items";
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);
        setList();
    }

    @Override
    protected void onListItemClick(ListView list, View view, int pos, long id){
        super.onListItemClick(list, view, pos, id);
        String item = adapter.getItem(pos);
        Toast.makeText(this, "Hoppsan! Du tryckte på " + item, Toast.LENGTH_LONG).show();
    }
    private void setList(){
        String[] stuff = getIntent().getStringArrayExtra(ITEMS);
        adapter = new ArrayAdapter<>(this, R.layout.row_layout, R.id.itemName, stuff);
        setListAdapter(adapter);
    }
}
