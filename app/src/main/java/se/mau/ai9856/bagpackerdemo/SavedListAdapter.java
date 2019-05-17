package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.ArrayList;

public class SavedListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<SubList> expList;

    public SavedListAdapter(Context context, ArrayList<SubList> expList) {
        this.context = context;
        this.expList = expList;
    }

    @Override
    public int getGroupCount() {
        return expList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Packable> itemList = expList.get(groupPosition).getItemList();
        return itemList.size();
    }

    @Override
    public SubList getGroup(int groupPosition) {
        return expList.get(groupPosition);
    }

    @Override
    public Packable getChild(int groupPosition, int childPosition) {
        ArrayList<Packable> itemList = expList.get(groupPosition).getItemList();
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
        SubList group = getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.header_layout, null);
        }
        TextView header = view.findViewById(R.id.categoryName);
        header.setText(group.getName().trim());
        TextView categoryTotal = view.findViewById(R.id.categoryTotal);
        int childCount = getChildrenCount(groupPosition);
        int selectedChildren = 0;
        for(int i = 0; i < childCount; i++){
            if(getChild(groupPosition, i).isSelected){
                selectedChildren++;
            }
        }
        categoryTotal.setText(selectedChildren + "/" + childCount);
        if(selectedChildren == childCount){

        }
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {
        final ViewHolder holder;
        final Packable packable = getChild(groupPosition, childPosition);
        if (view == null) {

            holder = new ViewHolder();
            LayoutInflater childInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = childInflater.inflate(R.layout.saved_row_layout, null);
            holder.tvName = view.findViewById(R.id.itemName);
            holder.tvQuantity = view.findViewById(R.id.itemQuantity);
            holder.checks = view.findViewById(R.id.itemCheck);
            view.setTag(holder);
        } else {
            if (view.getTag() instanceof ViewHolder) {
                holder = (ViewHolder) view.getTag();
            } else {
                holder = new ViewHolder();
            }
        }
        holder.checks.setOnCheckedChangeListener(null);
        holder.checks.setFocusable(true);

        if (packable.isSelected) {
            holder.checks.setChecked(true);
        } else {
            holder.checks.setChecked(false);
        }

        holder.checks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (packable.isSelected && isChecked) {
                    holder.checks.setChecked(false);
                    packable.isSelected = false;
                } else {
                    holder.checks.setChecked(true);
                    packable.isSelected = true;
                }
                if (isChecked) {
                    packable.isSelected = true;
                } else {
                    packable.isSelected = false;
                }
                notifyDataSetChanged();
                Database.saveList(context, "LIST", expList);
            }
        });
        holder.tvName.setText(packable.getItemName().trim());
        if(packable.getQuantity() > 1){
            holder.tvQuantity.setVisibility(View.VISIBLE);
            holder.tvQuantity.setText("" + packable.getQuantity());
        } else {
            holder.tvQuantity.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class ViewHolder {
        private TextView tvName;
        private CheckBox checks;
        private TextView tvQuantity;
    }
}

