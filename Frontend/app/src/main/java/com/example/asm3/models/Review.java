package com.example.asm3.models;

import java.io.Serializable;

public class Review implements Serializable {
    private String _id;
    private String content;
    private String timestamp;
    private float rating;
    private Customer reviewer;
    private Order order;

    public Review(String _id, String content, String timestamp, float rating, Customer reviewer, Order order) {
        this._id = _id;
        this.content = content;
        this.timestamp = timestamp;
        this.rating = rating;
        this.reviewer = reviewer;
        this.order = order;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Customer getReviewer() {
        return reviewer;
    }

    public void setReviewer(Customer reviewer) {
        this.reviewer = reviewer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
