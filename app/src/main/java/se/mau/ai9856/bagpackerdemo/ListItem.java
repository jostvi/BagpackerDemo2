package se.mau.ai9856.bagpackerdemo;

public class ListItem {
    private int position = 0;
    private String name = null;
    protected boolean isSelected = false;

    public ListItem(int position, String name){

        this.position = position;
        this.name = name;
    }

    public void setItemName(String name){
        this.name = name;
    }

    public String getItemName(){
        return name;
    }
}
