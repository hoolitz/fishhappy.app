package tz.co.fishhappy.app.model;

import java.util.List;

/**
 * Created by Simon on 14-May-17.
 */

public class CartRequestModel {

    private List<CartProductRequestModel> products = null;


    public CartRequestModel(List<CartProductRequestModel> products) {
        this.products = products;
    }

    public List<CartProductRequestModel> getProducts() {
        return products;
    }

    public void setProducts(List<CartProductRequestModel> products) {
        this.products = products;
    }
}
