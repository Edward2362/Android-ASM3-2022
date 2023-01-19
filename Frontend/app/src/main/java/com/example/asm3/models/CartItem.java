package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int quantity;
    private Book product;
    private boolean isProductPopulated = false;

    public boolean isProductPopulated() {
        return isProductPopulated;
    }

    public void setIsProductPopulated(boolean isProductPopulated) {
        this.isProductPopulated = isProductPopulated;
    }

    public static String quantityKey="quantity";
    public static String productKey="product";

    public CartItem(int quantity, Book product) {
        this.quantity = quantity;
        this.product = product;
    }

    public CartItem() {
        this.quantity = 0;
        this.product = null;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Book getProduct() {
        return product;
    }

    public void setProduct(Book product) {
        this.product = product;
    }

    public static CartItem fromJSON(JSONObject jsonObject) {
        CartItem cartItem = new CartItem();

        if (jsonObject != null) {
            try {
                cartItem.setQuantity(jsonObject.getInt(quantityKey));
                if (jsonObject.get(productKey) instanceof JSONObject) {
                    Book product = Book.fromJSON(jsonObject.getJSONObject(productKey));
                    cartItem.setProduct(product);
                    cartItem.setIsProductPopulated(true);
                } else if (jsonObject.get(productKey) instanceof String) {
                    Book product = new Book();
                    product.set_id(jsonObject.getString(productKey));
                    cartItem.setProduct(product);
                }
            } catch (JSONException jsonException){
                jsonException.printStackTrace();
            }
        }
        return cartItem;
    }
    public static JSONObject toJSON(CartItem cartItem) {
        JSONObject jsonObject = null;
        if (cartItem != null) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put(quantityKey, cartItem.quantity);

                if (cartItem.isProductPopulated()) {
                    jsonObject.put(productKey, Book.toJSON(cartItem.product));
                } else {
                    jsonObject.put(productKey, cartItem.product.get_id());
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }
}
