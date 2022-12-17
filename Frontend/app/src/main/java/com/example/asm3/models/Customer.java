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
                username = jsonObject.getString("username");
                password = jsonObject.getString("password");
                firstName = jsonObject.getString("first_name");
                lastName = jsonObject.getString("last_name");
                address = jsonObject.getString("address");
                role = jsonObject.getString("role");
                rating = (float) jsonObject.getDouble("ratings");
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
                jsonObject.put("username", customer.username);
                jsonObject.put("password", customer.password);
                jsonObject.put("first_name", customer.firstName);
                jsonObject.put("last_name", customer.lastName);
                jsonObject.put("address", customer.address);
                jsonObject.put("role", customer.role);
                jsonObject.put("ratings", customer.rating);
            } catch(JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }
}
