package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Category implements Serializable {
    private String name;
    private String _id;

    public static String nameKey="name";
    public static String idKey="_id";

    public Category(String name, String _id) {
        this.name = name;
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public static Category fromJSON(JSONObject jsonObject) {
        Category category = null;
        String name = "";
        String _id = "";
        if (jsonObject != null) {
            try {
                name = jsonObject.getString(nameKey);
                _id = jsonObject.getString(idKey);
                category = new Category(name, _id);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return category;
    }

    public static JSONObject toJSON(Category category) {
        JSONObject jsonObject = null;
        if (category != null) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put(nameKey, category.name);
                jsonObject.put(idKey, category._id);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }
}