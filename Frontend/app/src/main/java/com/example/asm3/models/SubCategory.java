package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SubCategory implements Serializable {
    private String name;
    private String _id;
    private boolean isChosen = false;

    public static String nameKey = "name";
    public static String idKey = "_id";

    public SubCategory(String name, String _id) {
        this.name = name;
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public static SubCategory fromJSON(JSONObject jsonObject) {
        SubCategory subCategory = null;
        String name = "";
        String _id = "";
        if (jsonObject != null) {
            try {
                name = jsonObject.getString(nameKey);
                _id = jsonObject.getString(idKey);
                subCategory = new SubCategory(name, _id);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return subCategory;
    }

    public static JSONObject toJSON(SubCategory subCategory) {
        JSONObject jsonObject = null;
        if (subCategory != null) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put(nameKey, subCategory.name);
                jsonObject.put(idKey, subCategory._id);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }
}
