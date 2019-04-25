package se.mau.ai9856.bagpackerdemo;

import java.util.ArrayList;

public class Header {
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
}
