package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private String bookName;
    private float bookPrice;
    private int quantity;
    private String order;

    public static String bookNameKey = "bookName";
    public static String bookPriceKey = "bookPrice";
    public static String quantityKey = "quantity";
    public static String orderKey = "order";

    public OrderDetail(String bookName, float bookPrice, int quantity, String order) {
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.quantity = quantity;
        this.order = order;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public float getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(float bookPrice) {
        this.bookPrice = bookPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public static String getBookNameKey() {
        return bookNameKey;
    }

    public static void setBookNameKey(String bookNameKey) {
        OrderDetail.bookNameKey = bookNameKey;
    }

    public static String getBookPriceKey() {
        return bookPriceKey;
    }

    public static void setBookPriceKey(String bookPriceKey) {
        OrderDetail.bookPriceKey = bookPriceKey;
    }

    public static String getQuantityKey() {
        return quantityKey;
    }

    public static void setQuantityKey(String quantityKey) {
        OrderDetail.quantityKey = quantityKey;
    }

    public static String getOrderKey() {
        return orderKey;
    }

    public static void setOrderKey(String orderKey) {
        OrderDetail.orderKey = orderKey;
    }

    public static OrderDetail fromJSON(JSONObject jsonObject) {
        OrderDetail orderDetail = null;
        String bookName = "";
        float bookPrice;
        int quantity;
        String order = "";

        if (jsonObject != null) {
            try {
                bookName = jsonObject.getString(bookNameKey);
                bookPrice = (float) jsonObject.getDouble(bookPriceKey);
                quantity = jsonObject.getInt(quantityKey);
                order = jsonObject.getString(orderKey);
                orderDetail = new OrderDetail(bookName, bookPrice, quantity,order);
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
        return orderDetail;
    }

    public static JSONObject toJSON(OrderDetail orderDetail) {
        JSONObject jsonObject = null;
        if (orderDetail != null) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put(bookNameKey, orderDetail.bookName);
                jsonObject.put(bookPriceKey, orderDetail.bookPrice);
                jsonObject.put(quantityKey, orderDetail.quantity);
                jsonObject.put(orderKey, orderDetail.order);
            } catch(JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }
}
