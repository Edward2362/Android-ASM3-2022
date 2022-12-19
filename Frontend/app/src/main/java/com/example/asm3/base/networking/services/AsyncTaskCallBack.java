package com.example.asm3.base.networking.services;

import android.content.Context;
import android.widget.Toast;

import com.example.asm3.base.networking.api.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

public interface AsyncTaskCallBack {
    public void onFinished(String message, String taskType);
    public static void verifyMessage(Context context, String message, String taskType, AsyncTaskCallBack callBack) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            if (jsonObject.getBoolean(ApiService.errorKey)) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            } else {
                callBack.onFinished(message, taskType);
            }
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
