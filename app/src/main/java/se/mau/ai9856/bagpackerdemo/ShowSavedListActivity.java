package se.mau.ai9856.bagpackerdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ShowSavedListActivity extends AppCompatActivity {
    private final static String ITEMS = "items";
    private final static String NAME = "name";
    private final static String INFO = "info";
    private ExpandableListView expandableListView;
    private SavedListAdapter savedListAdapter;
    private String name, info;
    private final String listKey = "LIST";
    private final String nameKey = "NAME";
    private final String infoKey = "INFO";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        initializeComponents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
        Button btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowSavedListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeComponents() {
        name = Database.loadName(this, nameKey);
        info = Database.loadInfo(this, infoKey);
        ArrayList<SubList> expandableList = Database.loadList(this, listKey);
        setContentView(R.layout.activity_show_saved);
        expandableListView = findViewById(R.id.SavedexpListView);
        TextView title = findViewById(R.id.titleTextView);
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowSavedListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        title.setText(name);

        if (expandableList == null) {
            finish();
            Toast.makeText(this, "Du har inga sparade listor :(",
                    Toast.LENGTH_SHORT).show();
        } else {
            savedListAdapter = new SavedListAdapter
                    (ShowSavedListActivity.this, expandableList);
            expandableListView.setAdapter(savedListAdapter);
            expandAll();
        }
    }

    private void expandAll() {
        int count = savedListAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            if(savedListAdapter.getGroup(i).getExpanded())
                expandableListView.expandGroup(i);
        }
    }

    public void deleteList(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowSavedListActivity.this);
        builder.setCancelable(true);
        builder.setMessage("Är du säker på att du vill radera \"" + name + "\"?");
        builder.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Radera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Database.deleteList(ShowSavedListActivity.this, listKey);
                Database.deleteName(ShowSavedListActivity.this, nameKey);
                Database.deleteInfo(ShowSavedListActivity.this, infoKey);
                Intent intent = new Intent(ShowSavedListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

    public void goToEditMode(View v) {
        Intent intent = new Intent(this, EditableListActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(Database.loadList(this, listKey));
        String name = Database.loadName(this, nameKey);
        String info = Database.loadInfo(this, infoKey);
        intent.putExtra(ITEMS, json);
        intent.putExtra(NAME, name);
        intent.putExtra(INFO, info);
        startActivity(intent);
    }

    public void showPopupInfo(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.info_popup, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        TextView popupText = popupWindow.getContentView().findViewById(R.id.popupText);
        popupText.setText(info);
        popupWindow.showAtLocation(v, Gravity.TOP, 0, 0);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
