package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Customer implements Serializable {
    private String email;
    private String password;
    private String username;
    private String address;
    private String role;
    private float rating;

    public static String emailKey = "email";
    public static String passwordKey = "password";
    public static String usernameKey = "username";
    public static String addressKey = "address";
    public static String roleKey = "role";

    public static String ratingsKey = "ratings";
    public static String tokenKey = "token";
    public static String customerRole = "CUSTOMER_ROLE";

    public Customer(String email, String password, String username, String address, String role, float rating) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.address = address;
        this.role = role;
        this.rating = rating;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public static Customer fromJSON(JSONObject jsonObject) {
        Customer customer = null;
        String email = "";
        String password = "";
        String username = "";
        String address = "";
        String role = "";
        float rating = 0F;
        if (jsonObject != null) {
            try {
                email = jsonObject.getString(emailKey);
                password = jsonObject.getString(passwordKey);
                username = jsonObject.getString(usernameKey);
                address = jsonObject.getString(addressKey);
                role = jsonObject.getString(roleKey);
                rating = (float) jsonObject.getDouble(ratingsKey);
                customer = new Customer(email, password, username, address, role, rating);
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
                jsonObject.put(ratingsKey, customer.rating);
            } catch(JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }
}
