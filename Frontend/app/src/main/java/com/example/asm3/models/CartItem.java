package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CartItem {
    private int quantity;
    private Book product;

    public static String quantityKey="quantity";
    public static String productKey="product";

    public CartItem(int quantity, Book product) {
        this.quantity = quantity;
        this.product = product;
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
        CartItem cartItem = null;
        int quantity;
        Book product;
        if (jsonObject != null) {
            try {
                quantity = jsonObject.getInt(quantityKey);
                product = Book.fromJSON(jsonObject.getJSONObject(productKey));
                cartItem = new CartItem(quantity,product);
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
                jsonObject.put(productKey, cartItem.product.get_id());

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }
}
