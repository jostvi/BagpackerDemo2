package se.mau.ai9856.bagpackerdemo;

import java.util.ArrayList;

public class Header {
    private String name;
    private ArrayList<ListItem> itemList = new ArrayList<>();

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<ListItem> getItemList(){
        return itemList;
    }

    public void setItemList(ArrayList<ListItem> itemList){ this.itemList = itemList; }
}
