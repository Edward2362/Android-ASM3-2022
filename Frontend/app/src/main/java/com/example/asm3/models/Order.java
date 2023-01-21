package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Order implements Serializable {
    private String _id;
    private String timestamp;
    private String status;
    private Customer buyer, seller;
    private String bookImage;
    private String bookName;
    private float bookPrice;
    private int quantity;
    private boolean hasReview;
    private boolean isBuyerPopulated = false;
    private boolean isSellerPopulated = false;


    public boolean isBuyerPopulated() {
        return isBuyerPopulated;
    }


    public void setIsBuyerPopulated(boolean isBuyerPopulated) {
        this.isBuyerPopulated = isBuyerPopulated;
    }

    public boolean isSellerPopulated() {
        return isSellerPopulated;
    }


    public void setIsSellerPopulated(boolean isSellerPopulated) {
        this.isSellerPopulated = isSellerPopulated;
    }

    public static String idKey = "_id";
    public static String timestampKey = "timeStamp";
    public static String statusKey = "status";
    public static String buyerKey = "buyer";
    public static String sellerKey = "seller";
    public static String bookNameKey = "bookName";
    public static String bookImageKey = "bookImage";
    public static String bookPriceKey = "bookPrice";
    public static String quantityKey = "quantity";
    public static String hasReviewKey = "hasReview";

    public Order(String _id, String timestamp, String status, Customer buyer, Customer seller, String bookImage, String bookName, float bookPrice, int quantity, boolean hasReview) {
        this._id = _id;
        this.timestamp = timestamp;
        this.status = status;
        this.buyer = buyer;
        this.seller = seller;
        this.bookImage = bookImage;
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.quantity = quantity;
        this.hasReview = hasReview;
    }

    public Order() {
        this._id = "";
        this.timestamp = "";
        this.status = "";
        this.buyer = null;
        this.seller = null;
        this.bookImage = "";
        this.bookName = "";
        this.bookPrice = 0f;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getBuyer() {
        return buyer;
    }

    public void setBuyer(Customer buyer) {
        this.buyer = buyer;
    }

    public Customer getSeller() {
        return seller;
    }

    public void setSeller(Customer seller) {
        this.seller = seller;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
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

    public static Order fromJSON(JSONObject jsonObject) {
        Order order = new Order();


        if (jsonObject != null) {
            try {
                order.set_id(jsonObject.getString(idKey));
                order.setTimestamp(jsonObject.getString(timestampKey));
                order.setStatus(jsonObject.getString(statusKey));
                if (jsonObject.get(sellerKey) instanceof JSONObject) {
                    Customer seller = Customer.fromJSON(jsonObject.getJSONObject(sellerKey));
                    order.setSeller(seller);
                    order.setIsSellerPopulated(true);
                } else if (jsonObject.get(sellerKey) instanceof String) {
                    Customer seller = new Customer();
                    seller.set_id(jsonObject.getString(sellerKey));
                    order.setSeller(seller);
                }
                if (jsonObject.get(buyerKey) instanceof JSONObject) {
                    Customer buyer = Customer.fromJSON(jsonObject.getJSONObject(buyerKey));
                    order.setBuyer(buyer);
                    order.setIsBuyerPopulated(true);
                } else if (jsonObject.get(buyerKey) instanceof String) {
                    Customer buyer = new Customer();
                    buyer.set_id(jsonObject.getString(buyerKey));
                    order.setBuyer(buyer);
                }
                order.setBookImage(jsonObject.getString(bookImageKey));
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
                if (order.isSellerPopulated()) {
                    jsonObject.put(sellerKey, Customer.toJSON(order.seller));
                } else {
                    jsonObject.put(sellerKey, order.seller.get_id());
                }
                if (order.isBuyerPopulated()) {
                    jsonObject.put(buyerKey, Customer.toJSON(order.buyer));
                } else {
                    jsonObject.put(buyerKey, order.buyer.get_id());
                }
                jsonObject.put(bookImageKey, order.bookImage);
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
