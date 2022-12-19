package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Customer implements Serializable {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String role;
    private float rating;
    public static String usernameKey = "username";
    public static String passwordKey = "password";
    public static String firstNameKey = "first_name";
    public static String lastNameKey = "last_name";
    public static String addressKey = "address";
    public static String roleKey = "role";

    public static String ratingsKey = "ratings";
    public static String tokenKey = "token";
    public static String customerRole = "CUSTOMER_ROLE";

    public Customer(String username, String password, String firstName, String lastName, String address, String role, float rating) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.role = role;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        String username = "";
        String password = "";
        String firstName = "";
        String lastName = "";
        String address = "";
        String role = "";
        float rating = 0F;
        if (jsonObject != null) {
            try {
                username = jsonObject.getString(usernameKey);
                password = jsonObject.getString(passwordKey);
                firstName = jsonObject.getString(firstNameKey);
                lastName = jsonObject.getString(lastNameKey);
                address = jsonObject.getString(addressKey);
                role = jsonObject.getString(roleKey);
                rating = (float) jsonObject.getDouble(ratingsKey);
                customer = new Customer(username, password, firstName, lastName, address, role, rating);
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
                jsonObject.put(usernameKey, customer.username);
                jsonObject.put(passwordKey, customer.password);
                jsonObject.put(firstNameKey, customer.firstName);
                jsonObject.put(lastNameKey, customer.lastName);
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
