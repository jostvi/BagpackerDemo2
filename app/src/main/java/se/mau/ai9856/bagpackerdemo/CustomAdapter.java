package se.mau.ai9856.bagpackerdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<ListItem> list;

    public CustomAdapter(Context context, ArrayList<ListItem> list){
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ListItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_layout, null);
            holder.tv = convertView.findViewById(R.id.itemName);
            holder.checks = convertView.findViewById(R.id.itemCheck);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.checks.setOnCheckedChangeListener(null);
        holder.checks.setFocusable(true);

        if (list.get(position).isSelected){
            holder.checks.setChecked(true);
        } else {
            holder.checks.setChecked(false);
        }

        holder.checks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean b) {
                if (checkMaxLimit()) {
                    if (list.get(position).isSelected && b) {
                        holder.checks.setChecked(false);
                        list.get(position).isSelected = false;
                    } else {
                      //  holder.checks.setChecked(false);
                        // list.get(position).isSelected = false;
                        Toast.makeText(context, "Packat och klart!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (b) {
                        list.get(position).isSelected = true;
                    } else {
                        list.get(position).isSelected = false;
                    }
                }
            }
        });

        holder.tv.setText(list.get(position).getItemName());
        return convertView;
    }

    public boolean checkMaxLimit(){
        int counterMax = 0;
        for(ListItem item : list){
            if(item.isSelected){
                counterMax++;
            }
        }
        return counterMax >= list.size()-1;
    }
    private class ViewHolder{
        TextView tv;
        public CheckBox checks;
    }
}
