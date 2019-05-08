package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<SubList> expList;

    public EditableListAdapter(Context context, ArrayList<SubList> expList) {
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
        TextView header = view.findViewById(R.id.list_header);
        header.setText(group.getName().trim());

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
            view = childInflater.inflate(R.layout.editable_row_layout, null);
            holder.tvName = view.findViewById(R.id.itemName);
            holder.btnDelete = view.findViewById(R.id.deleteButton);
            holder.btnSubtract = view.findViewById(R.id.subQuantity);
            holder.btnAdd = view.findViewById(R.id.addQuantity);
            holder.tvQuantity = view.findViewById(R.id.itemQuantity);
            view.setTag(holder);

        } else {
            if (view.getTag() instanceof ViewHolder) {
                holder = (ViewHolder) view.getTag();
            } else {
                holder = new ViewHolder();
            }
        }
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGroup(groupPosition).getItemList().remove(packable);
                notifyDataSetChanged();
                Toast.makeText(context,"Du har tagit bort " +
                        packable.getItemName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnSubtract.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                packable.decrease();
                notifyDataSetChanged();
            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                packable.increase();
                notifyDataSetChanged();
            }
        });
        holder.tvName.setText(packable.getItemName().trim());
        holder.tvQuantity.setText("" + packable.getQuantity());

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class ViewHolder {
        private TextView tvName;
        private ImageButton btnDelete;
        private Button btnSubtract;
        private Button btnAdd;
        private TextView tvQuantity;
    }
}
