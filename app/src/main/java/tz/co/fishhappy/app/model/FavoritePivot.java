package tz.co.fishhappy.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Simon on 15-May-17.
 */

public class FavoritePivot {
    @SerializedName("customer_id")
    @Expose
    private int customerId;
    @SerializedName("product_id")
    @Expose
    private int productId;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

}
