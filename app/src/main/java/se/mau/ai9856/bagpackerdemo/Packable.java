package se.mau.ai9856.bagpackerdemo;

public class Packable {
    private final String name;
    private int quantity;
    private float weight;
    protected boolean isSelected = false;

    public Packable(String name, int quantity, float weight){
        this.name = name;
        this.quantity = quantity;
        this.weight  =weight;
    }

    public String getItemName(){
        return name;
    }

    public void increase(){
        if(quantity < 99)
            quantity++;
    }

    public void decrease(){
        if(quantity > 1)
            quantity--;
    }

    public int getQuantity(){
        return quantity;
    }

    public float getWeight(){
        return weight;
    }
}
