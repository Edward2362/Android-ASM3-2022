package com.example.asm3.base.controller;

import android.app.Activity;
import android.content.Context;

public abstract class BaseController {
    private Context context;
    private Activity activity;

    public BaseController(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public abstract void onInit();
}
