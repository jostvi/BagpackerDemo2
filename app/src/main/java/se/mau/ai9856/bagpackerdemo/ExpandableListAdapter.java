package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<HeaderInfo> sectionList;

    public ExpandableListAdapter(Context context, ArrayList<HeaderInfo> sectionList){
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
    public HeaderInfo getGroup(int groupPosition) {
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
        HeaderInfo headerInfo = getGroup(groupPosition);
        if(view == null){
            LayoutInflater inf = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.header_layout, null);
        }

        TextView header = view.findViewById(R.id.listHeader);
        header.setText(headerInfo.getName().trim());
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {
        final ViewHolder holder;
        final ListItem listItem = getChild(groupPosition, childPosition);
        if (view == null){
            holder = new ViewHolder();
            LayoutInflater childInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = childInflater.inflate(R.layout.row_layout, null);
            holder.tv = view.findViewById(R.id.itemName);
            holder.checks = view.findViewById(R.id.itemCheck);
            holder.btnDelete = view.findViewById(R.id.deleteButton);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
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
}
