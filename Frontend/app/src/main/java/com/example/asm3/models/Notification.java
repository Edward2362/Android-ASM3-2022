package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Notification {
    private String _id;
    private Customer customer;
    private String content;
    private String timestamp;
    private boolean isRead;
    private boolean isCustomerPopulated = false;

    public boolean isCustomerPopulated() {
        return isCustomerPopulated;
    }

    public void setIsCustomerPopulated(boolean isCustomerPopulated) {
        this.isCustomerPopulated = isCustomerPopulated;
    }


    public static String idKey = "_id";
    public static String customerKey = "customer";
    public static String contentKey = "content";
    public static String timestampKey = "timestamp";
    public static String isReadKey = "isRead";

    public Notification(String _id, Customer customer, String content, String timestamp, boolean isRead) {
        this._id = _id;
        this.customer = customer;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public Notification() {
        this._id = "";
        this.customer = null;
        this.content = "";
        this.timestamp = "";
        this.isRead = false;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public static String getIdKey() {
        return idKey;
    }

    public static void setIdKey(String idKey) {
        Notification.idKey = idKey;
    }

    public static String getCustomerKey() {
        return customerKey;
    }

    public static void setCustomerKey(String customerKey) {
        Notification.customerKey = customerKey;
    }

    public static String getContentKey() {
        return contentKey;
    }

    public static void setContentKey(String contentKey) {
        Notification.contentKey = contentKey;
    }

    public static String getTimestampKey() {
        return timestampKey;
    }

    public static void setTimestampKey(String timestampKey) {
        Notification.timestampKey = timestampKey;
    }

    public static String getIsReadKey() {
        return isReadKey;
    }

    public static void setIsReadKey(String isReadKey) {
        Notification.isReadKey = isReadKey;
    }

    public static Notification fromJSON(JSONObject jsonObject) {
        Notification notification = new Notification();

        if (jsonObject != null) {
            try {

                notification.set_id(jsonObject.getString(idKey));
                if (jsonObject.get(customerKey) instanceof JSONObject) {
                    Customer customer = Customer.fromJSON(jsonObject.getJSONObject(customerKey));
                    notification.setCustomer(customer);
                    notification.setIsCustomerPopulated(true);
                } else if (jsonObject.get(customerKey) instanceof String) {
                    Customer customer = new Customer();
                    customer.set_id(jsonObject.getString(customerKey));
                    notification.setCustomer(customer);
                }

                notification.setContent(jsonObject.getString(contentKey));
                notification.setTimestamp(jsonObject.getString(timestampKey));
                notification.setIsRead(jsonObject.getBoolean(isReadKey));
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
        return notification;
    }

    public static JSONObject toJSON(Notification notification) {
        JSONObject jsonObject = null;
        if (notification != null) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put(idKey, notification._id);

                if (notification.isCustomerPopulated()) {
                    jsonObject.put(customerKey, Customer.toJSON(notification.customer));
                } else {
                    jsonObject.put(customerKey, notification.customer.get_id());
                }
                jsonObject.put(contentKey, notification.content);
                jsonObject.put(timestampKey, notification.timestamp);
                jsonObject.put(isReadKey, notification.isRead);
            } catch(JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }
}
