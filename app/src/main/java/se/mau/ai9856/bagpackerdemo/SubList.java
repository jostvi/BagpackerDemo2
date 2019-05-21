package se.mau.ai9856.bagpackerdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SubList {
    private String name;
    private ArrayList<Packable> itemList = new ArrayList<>();

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<Packable> getItemList(){
        return itemList;
    }

    public void setItemList(ArrayList<Packable> itemList){ this.itemList = itemList; }

    public void addItem(Packable item){
        itemList.add(item);
        Collections.sort(itemList, new Comparator<Packable>() {
            @Override
            public int compare(Packable o1, Packable o2) {
                return o1.getItemName().compareTo(o2.getItemName());
            }
        });
    }
}
