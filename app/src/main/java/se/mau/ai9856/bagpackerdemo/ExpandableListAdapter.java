package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Header> sectionList;

    public ExpandableListAdapter(Context context, ArrayList<Header> sectionList){
        this.context = context;
        this.sectionList = sectionList;
    }

    @Override
    public int getGroupCount() {
        return sectionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ListItem> itemList = sectionList.get(groupPosition).getItemList();
        return itemList.size();
    }

    @Override
    public Header getGroup(int groupPosition) {
        return sectionList.get(groupPosition);
    }

    @Override
    public ListItem getChild(int groupPosition, int childPosition) {
        ArrayList<ListItem> itemList = sectionList.get(groupPosition).getItemList();
        return itemList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View view, ViewGroup parent) {
        Header group = getGroup(groupPosition);
        if(view == null){
            LayoutInflater inf = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.header_layout, null);
        }
        Log.e("HEJ", "Group: " + group.getName());
        TextView header = view.findViewById(R.id.list_header);
        header.setText(group.getName().trim());
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {
        final ViewHolder holder;
        final ViewHolder2 holder2;
        final ListItem listItem = getChild(groupPosition, childPosition);
        if (view == null){
            if (isLastChild){
                holder2 = new ViewHolder2();
                LayoutInflater childInflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = childInflater.inflate(R.layout.new_item_row, null);
                holder2.etNewItem = view.findViewById(R.id.etNewItem);
                holder2.btnAddItem = view.findViewById(R.id.btnAddItem);
                view.setTag(holder2);
                return view;
            } else {
                holder = new ViewHolder();
                LayoutInflater childInflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = childInflater.inflate(R.layout.row_layout, null);
                holder.tv = view.findViewById(R.id.itemName);
                holder.checks = view.findViewById(R.id.itemCheck);
                holder.btnDelete = view.findViewById(R.id.deleteButton);
                view.setTag(holder);
            }
            //Log.e("NULL", "Group: " + groupPosition + " Child: " + childPosition + " isLast: " + isLastChild + " View: " + view.toString() + " Parent: " + parent.toString());
        } else {
            if(view.getTag()instanceof ViewHolder){
                holder = (ViewHolder) view.getTag();
            } else {
                holder = new ViewHolder();
                holder2 = (ViewHolder2) view.getTag();
            }
        }
        holder.checks.setOnCheckedChangeListener(null);
        holder.checks.setFocusable(true);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGroup(groupPosition).getItemList().remove(listItem);
                notifyDataSetChanged();
            }
        });
        if (listItem.isSelected){
            holder.checks.setChecked(true);
        } else {
            holder.checks.setChecked(false);
        }

        holder.checks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if(checkMaxLimit()){
                    if (listItem.isSelected && isChecked){
                        holder.checks.setChecked(false);
                        listItem.isSelected = false;
                    } else {
                        holder.checks.setChecked(true);
                        listItem.isSelected = true;
                    }
                //} else {
                if (isChecked){
                    listItem.isSelected = true;
                } else {
                    listItem.isSelected = false;
                }
            }
        });
        Log.e("HEJ2", "Child: " + listItem.getItemName());
        holder.tv.setText(listItem.getItemName().trim());
        //TextView childItem = view.findViewById(R.id.itemName);
        //childItem.setText(listItem.getItemName().trim());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /*public boolean checkMaxLimit(){
        //  ArrayList<ListItem> list =
        int counterMAx = 0;
        for(ListItem item : list){
            if(item.isSelected){
                counterMax++;
            }
        }
        return counterMax >= list.size() - 1;
    }*/

    private class ViewHolder {
        private TextView tv;
        private CheckBox checks;
        private ImageButton btnDelete;
    }

    private class ViewHolder2 {
        private EditText etNewItem;
        private ImageButton btnAddItem;
    }
}
