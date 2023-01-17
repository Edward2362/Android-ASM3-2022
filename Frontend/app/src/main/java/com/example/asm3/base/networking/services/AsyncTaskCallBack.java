package com.example.asm3.base.networking.services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.asm3.base.networking.api.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

public interface AsyncTaskCallBack {
    public void onFinished(String message, String taskType);
    public void onError(String taskType);
    public static void verifyMessage(Context context, String message, String taskType, AsyncTaskCallBack callBack) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            if (jsonObject.getBoolean(ApiService.errorKey)) {
                callBack.onError(taskType);
            } else {
                callBack.onFinished(message, taskType);
            }
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
            Log.d(TAG, "verifyMessage: test in excep");
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
