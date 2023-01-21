package com.example.asm3.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiData<T> {
    private T data;

    public ApiData(T data) {
        this.data = data;
    }

    public ApiData() {}

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static <T> ApiData<T> fromJSON(JSONObject jsonObject, Class<T> t) {
        if (t.isAssignableFrom(Customer.class)) {
            return new ApiData<T>((T) Customer.fromJSON(jsonObject));
        } else if (t.isAssignableFrom(Category.class)) {
            return new ApiData<T>((T) Category.fromJSON(jsonObject));
        } else if (t.isAssignableFrom(SubCategory.class)) {
            return new ApiData<T>((T) SubCategory.fromJSON(jsonObject));
        } else if (t.isAssignableFrom(Book.class)){
            return new ApiData<T>((T) Book.fromJSON(jsonObject));
        } else if (t.isAssignableFrom(Notification.class)) {
            return new ApiData<T>((T) Notification.fromJSON(jsonObject));
        } else if (t.isAssignableFrom(Order.class)) {
            return new ApiData<T>((T) Order.fromJSON(jsonObject));
        } else if (t.isAssignableFrom(OrderDetail.class)) {
            return new ApiData<T>((T) OrderDetail.fromJSON(jsonObject));
        } else if (t.isAssignableFrom(Review.class)) {
            return new ApiData<T>((T) Review.fromJSON(jsonObject));
        } else if (t.isAssignableFrom(CartItem.class)) {
            return new ApiData<T>((T) CartItem.fromJSON(jsonObject));
        }
        else {
            return null;
        }
    }

    public static JSONObject getData(String message) {
        JSONObject jsonObject = null;
        JSONObject jsonMessage = null;
        JSONArray data = null;
        try {
            jsonMessage = new JSONObject(message);
            data = jsonMessage.getJSONArray("data");

            if (data.length() != 0) {
                jsonObject = data.getJSONObject(0);
            }
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return jsonObject;
    }
}
