package tz.co.fishhappy.app.model;

/**
 * Created by Simon on 04-May-17.
 */

public class CartModel {

    private String name;
    private int price;
    private int quantity;

    public CartModel(){}

    public CartModel(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
