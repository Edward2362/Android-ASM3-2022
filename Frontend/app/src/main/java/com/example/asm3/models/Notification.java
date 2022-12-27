package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Notification {
    private String _id;
    private String customer;
    private String content;
    private String timestamp;
    private boolean isRead;

    public static String idKey = "_id";
    public static String customerKey = "customer";
    public static String contentKey = "content";
    public static String timestampKey = "timestamp";
    public static String isReadKey = "isRead";

    public Notification(String _id, String customer, String content, String timestamp, boolean isRead) {
        this._id = _id;
        this.customer = customer;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
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

    public void setRead(boolean read) {
        isRead = read;
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
        Notification notification = null;
        String _id = "";
        String customer = "";
        String content = "";
        String timestamp = "";
        boolean isRead = false;
        if (jsonObject != null) {
            try {
                _id = jsonObject.getString(idKey);
                customer = jsonObject.getString(customerKey);
                content = jsonObject.getString(contentKey);
                timestamp = jsonObject.getString(timestampKey);
                isRead = jsonObject.getBoolean(isReadKey);
                notification = new Notification(_id,customer,content,timestamp,isRead);
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
                jsonObject.put(customerKey, notification.customer);
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
