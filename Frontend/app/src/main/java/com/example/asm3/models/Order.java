package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Order implements Serializable {
    private String _id;
    private String timestamp;
    private String status;
    private Customer customer;
    private String bookName;
    private float bookPrice;
    private int quantity;
    private boolean hasReview;
    private boolean isCustomerPopulated = false;

    public boolean isCustomerPopulated() {
        return isCustomerPopulated;
    }

    public void setIsCustomerPopulated(boolean isCustomerPopulated) {
        this.isCustomerPopulated = isCustomerPopulated;
    }

    public static String idKey = "_id";
    public static String timestampKey = "timestamp";
    public static String statusKey = "status";
    public static String customerKey = "customer";
    public static String bookNameKey = "bookName";
    public static String bookPriceKey = "bookPrice";
    public static String quantityKey = "quantity";
    public static String hasReviewKey = "hasReview";

    public Order(String _id, String timestamp, String status, Customer customer, String bookName, float bookPrice, int quantity, boolean hasReview) {
        this._id = _id;
        this.timestamp = timestamp;
        this.status = status;
        this.customer = customer;
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.quantity = quantity;
        this.hasReview = hasReview;
    }

    public Order() {
        this._id = "";
        this.timestamp = "";
        this.status = "";
        this.customer = null;
        this.bookName = "";
        this.bookPrice = 0F;
        this.quantity = 0;
        this.hasReview = false;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static String getTimestampKey() {
        return timestampKey;
    }

    public static void setTimestampKey(String timestampKey) {
        Order.timestampKey = timestampKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public boolean isHasReview() {
        return hasReview;
    }

    public void setHasReview(boolean hasReview) {
        this.hasReview = hasReview;
    }

    public static String getCustomerKey() {
        return customerKey;
    }

    public static void setCustomerKey(String customerKey) {
        Order.customerKey = customerKey;
    }

    public static Order fromJSON(JSONObject jsonObject) {
        Order order = new Order();


        if (jsonObject != null) {
            try {
                order.set_id(jsonObject.getString(idKey));
                order.setTimestamp(jsonObject.getString(timestampKey));
                order.setStatus(jsonObject.getString(statusKey));
                if (jsonObject.get(customerKey) instanceof JSONObject) {
                    Customer customer = Customer.fromJSON(jsonObject.getJSONObject(customerKey));
                    order.setCustomer(customer);
                    order.setIsCustomerPopulated(true);
                } else if (jsonObject.get(customerKey) instanceof String) {
                    Customer customer = new Customer();
                    customer.set_id(jsonObject.getString(customerKey));
                    order.setCustomer(customer);
                }

                order.setBookName(jsonObject.getString(bookNameKey));
                order.setBookPrice((float) jsonObject.getDouble(bookPriceKey));
                order.setQuantity(jsonObject.getInt(quantityKey));
                order.setHasReview(jsonObject.getBoolean(hasReviewKey));
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
                jsonObject.put(idKey, order._id);
                jsonObject.put(timestampKey, order.timestamp);
                jsonObject.put(statusKey, order.status);
                if (order.isCustomerPopulated()) {
                    jsonObject.put(customerKey, Customer.toJSON(order.customer));
                } else {
                    jsonObject.put(customerKey, order.customer.get_id());
                }

                jsonObject.put(bookNameKey, order.bookName);
                jsonObject.put(bookPriceKey, order.bookPrice);
                jsonObject.put(quantityKey, order.quantity);
                jsonObject.put(hasReviewKey, order.hasReview);
            } catch(JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }
}
