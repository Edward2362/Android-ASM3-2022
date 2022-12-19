package com.example.asm3.base.networking.services;

import android.content.Context;
import android.os.AsyncTask;

import com.example.asm3.base.networking.api.ApiService;
import com.example.asm3.config.Constant;

import org.json.JSONObject;

public class GetData extends AsyncTask<JSONObject, String, String> {
    private String endPoint;
    private String taskType;
    private AsyncTaskCallBack callBack;
    private ApiService apiService;
    private Context context;

    public GetData(Context context, AsyncTaskCallBack callBack) {
        this.callBack = callBack;
        this.endPoint = "";
        this.taskType = "";

        this.apiService = new ApiService(Constant.baseDomain);
        this.context = context;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Override
    protected String doInBackground(JSONObject... data) {
        String message = apiService.getJSON(endPoint);
        return message;
    }

    @Override
    protected void onPostExecute(String message) {
        AsyncTaskCallBack.verifyMessage(context, message, taskType, callBack);
    }
}
