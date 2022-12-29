package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Order implements Serializable {
    private String timestamp;
    private String seller;
    private String customer;

    public static String timestampKey = "timestamp";
    public static String sellerKey = "seller";
    public static String customerKey = "customer";

    public Order(String timestamp, String seller, String customer) {
        this.timestamp = timestamp;
        this.seller = seller;
        this.customer = customer;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public static String getTimestampKey() {
        return timestampKey;
    }

    public static void setTimestampKey(String timestampKey) {
        Order.timestampKey = timestampKey;
    }

    public static String getSellerKey() {
        return sellerKey;
    }

    public static void setSellerKey(String sellerKey) {
        Order.sellerKey = sellerKey;
    }

    public static String getCustomerKey() {
        return customerKey;
    }

    public static void setCustomerKey(String customerKey) {
        Order.customerKey = customerKey;
    }

    public static Order fromJSON(JSONObject jsonObject) {
        Order order = null;
        String timestamp = "";
        String seller = "";
        String customer = "";

        if (jsonObject != null) {
            try {
                timestamp = jsonObject.getString(timestampKey);
                seller = jsonObject.getString(sellerKey);
                customer = jsonObject.getString(customerKey);

                order = new Order(timestamp, seller, customer);
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
        return order;
    }

    public static JSONObject toJSON(Order order) {
        JSONObject jsonObject = null;
        if (order != null) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put(timestampKey, order.timestamp);
                jsonObject.put(sellerKey, order.seller);
                jsonObject.put(customerKey, order.customer);
            } catch(JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }
}
