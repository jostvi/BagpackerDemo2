package se.mau.ai9856.bagpackerdemo;

public class Packable {
    private final String name;
    private int quantity;
    protected boolean isSelected = false;

    public Packable(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public String getItemName(){
        return name;
    }

    public void increase(){
        if(quantity < 100)
            quantity++;
    }

    public void decrease(){
        if(quantity > 1)
            quantity--;
    }

    public int getQuantity(){
        return quantity;
    }
}
