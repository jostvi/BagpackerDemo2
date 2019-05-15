package se.mau.ai9856.bagpackerdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class EditableListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String ITEMS = "items";
    private static final String NAME = "name";
    private static final String INFO = "info";
    private ExpandableListView expListView;
    private ArrayList<SubList> expList;
    private EditableListAdapter adapter;
    private EditText etNewItem;
    private String category, name, info;
    protected static boolean saved;

    @Override
    public void onBackPressed() {
        if (!saved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditableListActivity.this);
            builder.setCancelable(true);
            builder.setMessage("Vill du spara listan?");
            builder.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    exitEditing();
                }
            });
            builder.setPositiveButton("Spara", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    saveList();
                    exitEditing();
                }
            });
            builder.show();
        } else {
            exitEditing();
        }
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
    }

    private void initializeComponents() {
        Intent intent = getIntent();
        String list = intent.getStringExtra(ITEMS);
        name = intent.getStringExtra(NAME);
        info = intent.getStringExtra(INFO);
        Gson gson = new Gson();
        expList = gson.fromJson(list, new TypeToken<List<SubList>>() {
        }.getType());
        setContentView(R.layout.activity_edit_list);
        expListView = findViewById(R.id.expListView);
        etNewItem = findViewById(R.id.etNewItem);
        adapter = new EditableListAdapter(EditableListActivity.this, expList);
        expListView.setAdapter(adapter);
        saved = false;
        ImageButton btnSaveList = findViewById(R.id.btnSaveList);
        btnSaveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveList();
            }
        });
        etNewItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i == EditorInfo.IME_ACTION_DONE){
                    addItem();
                    handled = true;
                }
                return handled;
            }
        });
        if (name == null) {                                   // MARKER: Se över denna lösning
            name = Database.loadName(this, "NAME");
        }
        if (info == null) {
            info = Database.loadInfo(this, "INFO");
        }

        TextView title = findViewById(R.id.listTitle);
        title.setText(name);
        setUpSpinner();
        expandAll();
    }

    private void exitEditing() {
        Intent intent = getIntent();
        if (intent.getStringExtra("ACTIVITY1") != null) {
            intent = new Intent(this, ShowSavedListActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    public void addItem() {
        String newItem = etNewItem.getText().toString().toLowerCase().trim();
        boolean itemAdded = false;
        if (newItem.length() == 0 || category.isEmpty()) {

            etNewItem.setHint("Döp din grej!!!");
            if (newItem.length() >0 && category.isEmpty()) {
                Toast.makeText(this, "Välj en kategori",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            for (SubList subList : expList) {
                if (subList.getName().equals(category)) {
                    addItem(subList, newItem);
                    itemAdded = true;
                }
            }
            if (!itemAdded) {
                SubList subList = new SubList();
                subList.setName(category);
                addItem(subList, newItem);
                expList.add(subList);
                expListView.expandGroup(expList.indexOf(subList));
            }
        }
    }

    public void addItem(SubList subList, String newItem) {
        subList.addItem(new Packable(newItem, 1));
        adapter.notifyDataSetChanged();
        etNewItem.setText("");
        etNewItem.setHint("Lägg till föremål");
        Toast.makeText(this, "Du har lagt till: " + newItem, Toast.LENGTH_SHORT).show();
        saved = false;
    }

    public void saveList() {
        Database.saveName(this, "NAME", name);
        Database.saveList(this, "LIST", expList);
        Database.saveInfo(this, "INFO", info);
        Toast.makeText(this, "\"" + name + "\"" + " sparad", Toast.LENGTH_SHORT).show();
        saved = true;
    }

    private void expandAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expListView.expandGroup(i);
        }
    }

    public void setUpSpinner() {
        Spinner spinner = findViewById(R.id.spChooseCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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
    }      // Lägg till felmeddelande (om ingen kategori är vald)?
}