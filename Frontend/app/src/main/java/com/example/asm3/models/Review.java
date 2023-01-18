package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Review implements Serializable {
    private String _id;
    private String content;
    private String date;
    private float rating;
    private Customer customer;
    private Order order;
    private boolean isCustomerPopulated = false;
    private boolean isOrderPopulated = false;

    public boolean isCustomerPopulated() {
        return isCustomerPopulated;
    }

    public void setIsCustomerPopulated(boolean isCustomerPopulated) {
        this.isCustomerPopulated = isCustomerPopulated;
    }

    public boolean isOrderPopulated() {
        return isOrderPopulated;
    }

    public void setIsOrderPopulated(boolean isOrderPopulated) {
        this.isOrderPopulated = isOrderPopulated;
    }

    public static String idKey = "id";
    public static String contentKey = "content";
    public static String dateKey = "date";
    public static String ratingKey = "rating";
    public static String customerKey = "customer";
    public static String orderKey = "order";

    public Review(String _id, String content, String date, float rating, Customer customer, Order order) {
        this._id = _id;
        this.content = content;
        this.date = date;
        this.rating = rating;
        this.customer = customer;
        this.order = order;
    }


    public Review() {
        this._id = "";
        this.content = "";
        this.date = "";
        this.rating = 0F;
        this.customer = null;
        this.order = null;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public static Review fromJSON(JSONObject jsonObject) {
        Review review = new Review();
        try {
            review.set_id(jsonObject.getString(idKey));
            review.setContent(jsonObject.getString(contentKey));
            review.setDate(jsonObject.getString(dateKey));
            review.setRating((float) jsonObject.getDouble(ratingKey));
            if (jsonObject.get(customerKey) instanceof JSONObject) {
                Customer customer = Customer.fromJSON(jsonObject.getJSONObject(customerKey));
                review.setCustomer(customer);
                review.setIsCustomerPopulated(true);
            } else if (jsonObject.get(customerKey) instanceof String) {
                Customer customer = new Customer();
                customer.set_id(jsonObject.getString(customerKey));
                review.setCustomer(customer);
            }

            if (jsonObject.get(orderKey) instanceof JSONObject) {
                Order order = Order.fromJSON(jsonObject.getJSONObject(orderKey));
                review.setOrder(order);
                review.setIsOrderPopulated(true);
            } else if (jsonObject.get(orderKey) instanceof String) {
                Order order = new Order();
                order.set_id(jsonObject.getString(orderKey));
                review.setOrder(order);
            }
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
        }

        return review;
    }

    public static JSONObject toJSON(Review review) {
        JSONObject jsonObject = null;
        try {
            jsonObject.put(idKey, review._id);
            jsonObject.put(contentKey, review.content);
            jsonObject.put(dateKey, review.date);
            jsonObject.put(ratingKey, review.rating);

            if (review.isCustomerPopulated()) {
                jsonObject.put(customerKey, Customer.toJSON(review.customer));
            } else {
                jsonObject.put(customerKey, review.customer.get_id());
            }

            if (review.isOrderPopulated()) {
                jsonObject.put(orderKey, Order.toJSON(review.order));
            } else {
                jsonObject.put(orderKey, review.order.get_id());
            }
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
        }

        return jsonObject;
    }
}
