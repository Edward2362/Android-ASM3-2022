package com.example.asm3.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {
    private String _id;
    private String email;
    private String password;
    private String username;
    private String address;
    private String role;
    private float ratings;
    private ArrayList<CartItem> cart;
    private String avatar;
    private boolean isCartPopulated = false;


    public boolean isCartPopulated() {
        return isCartPopulated;
    }

    public void setIsCartPopulated(boolean isCartPopulated) {
        this.isCartPopulated = isCartPopulated;
    }

    public static String idKey = "_id";
    public static String emailKey = "email";
    public static String passwordKey = "password";
    public static String usernameKey = "username";
    public static String addressKey = "address";
    public static String roleKey = "role";
    public static String ratingsKey = "ratings";
    public static String cartKey = "cart";
    public static String avatarKey = "avatar";
    public static String tokenKey = "token";
    public static String customerRole = "CUSTOMER_ROLE";

    public Customer(String _id, String email, String password, String username, String address, String role, float ratings, ArrayList<CartItem> cart, String avatar) {
        this._id = _id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.address = address;
        this.role = role;
        this.ratings = ratings;
        this.cart = cart;
        this.avatar = avatar;
    }

    public Customer() {
        this._id = "";
        this.email = "";
        this.password = "";
        this.username = "";
        this.address = "";
        this.role = "";
        this.ratings = 0F;
        this.cart = new ArrayList<>();
        this.avatar = "";
    }

    public Customer(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public ArrayList<CartItem> getCart() {
        return cart;
    }

    public void setCart(ArrayList<CartItem> cart) {
        this.cart = cart;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static Customer fromJSON(JSONObject jsonObject) {
        Customer customer = new Customer();
        if (jsonObject != null) {
            try {
                customer.set_id(jsonObject.getString(idKey));
                customer.setEmail(jsonObject.getString(emailKey));
                customer.setPassword(jsonObject.getString(passwordKey));
                customer.setUsername(jsonObject.getString(usernameKey));
                customer.setAddress(jsonObject.getString(addressKey));
                customer.setRole(jsonObject.getString(roleKey));
                customer.setRatings((float) jsonObject.getDouble(ratingsKey));
                ArrayList<CartItem> cartItems = new ArrayList<CartItem>();
                JSONArray cartJSONArray = jsonObject.getJSONArray(cartKey);
                for (int i = 0; i < cartJSONArray.length(); ++i) {
                    cartItems.add(CartItem.fromJSON(cartJSONArray.getJSONObject(i)));
                }
                customer.setCart(cartItems);
                customer.setIsCartPopulated(true);
                customer.setAvatar(jsonObject.getString(avatarKey));
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
        return customer;
    }

    public static JSONObject toJSON(Customer customer) {
        JSONObject jsonObject = null;
        if (customer != null) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put(emailKey, customer.email);
                jsonObject.put(passwordKey, customer.password);
                jsonObject.put(usernameKey, customer.username);
                jsonObject.put(addressKey, customer.address);
                jsonObject.put(roleKey, customer.role);
                jsonObject.put(ratingsKey, customer.ratings);
                jsonObject.put(idKey, customer._id);
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < customer.cart.size(); ++i) {
                    jsonArray.put(CartItem.toJSON(customer.cart.get(i)));
                }
                jsonObject.put(cartKey, jsonArray);
                jsonObject.put(avatarKey, customer.avatar);
            } catch(JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }
}
