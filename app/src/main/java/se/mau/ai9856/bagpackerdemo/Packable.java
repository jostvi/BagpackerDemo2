package se.mau.ai9856.bagpackerdemo;

public class Packable {
    private final String name;
    protected boolean isSelected = false;

    public Packable(String name){
        this.name = name;
    }

    public String getItemName(){
        return name;
    }
}
