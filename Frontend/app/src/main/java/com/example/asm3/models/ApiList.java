package com.example.asm3.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiList<T extends Object> {
    private ArrayList<T> list;

    public ApiList(ArrayList<T> list) {
        this.list = list;
    }

    public ApiList() {}

    public ArrayList<T> getList() {
        return this.list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }

    public static <T> ApiList<T> fromJSON(JSONArray jsonArray, Class<T> t) {
        if (t.isAssignableFrom(Customer.class)) {
            ArrayList<Customer> customers = new ArrayList<Customer>();

            try {
                for (int i = 0; i < jsonArray.length(); ++i) {
                    customers.add(Customer.fromJSON(jsonArray.getJSONObject(i)));
                }
            } catch(Exception exception) {
                exception.printStackTrace();
            }

            return new ApiList<T>((ArrayList<T>) customers);
        } else {
            return null;
        }
    }

    public static JSONArray getData(String message) {
        JSONArray jsonArray = null;
        JSONObject jsonMessage = null;
        try {
            jsonMessage = new JSONObject(message);
            jsonArray = jsonMessage.getJSONArray("data");
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return jsonArray;
    }
}
