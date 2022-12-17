package com.example.asm3.base.networking.services;

import com.example.asm3.base.networking.api.ApiService;
import com.example.asm3.config.Constant;
import com.example.asm3.models.Customer;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthApi {
    private ApiService apiService;

    public AuthApi() {
        apiService = new ApiService(Constant.baseDomain);
    }

    public Customer getCustomer(String username, String password) {
        Customer customer = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.username, username);
            jsonObject.put(Constant.password, password);
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
        }
        String data = apiService.postJSON(Constant.getCustomerData, jsonObject);
        System.out.print(data);
        return customer;
    }
}
