package com.example.asm3.controllers;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.asm3.MainActivity;
import com.example.asm3.R;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.localStorage.LocalFileController;
import com.example.asm3.base.networking.api.ApiService;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.PostData;
import com.example.asm3.config.Constant;
import com.example.asm3.fragments.authenticationActivity.LoginFragment;
import com.example.asm3.fragments.authenticationActivity.RegisterFragment;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AuthenticationActivityController extends BaseController implements AsyncTaskCallBack {
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private PostData postData;


    public AuthenticationActivityController(Context context, FragmentActivity activity) {
        super(context, activity);

        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        loginFragment.setController(this);
        registerFragment.setController(this);

        postData = new PostData(getContext(), this);
    }

    // Render functions
    @Override
    public void onInit() {
        Intent intent = getActivity().getIntent();
        String mainFragment = "";
        if (intent.getExtras().get(Constant.mainFragment) != null) {
            mainFragment = (String) intent.getExtras().get(Constant.mainFragment);
        }

        if (mainFragment.equals(Constant.register)) {
            loadFragment(registerFragment);
        } else if (mainFragment.equals(Constant.login)) {
            loadFragment(loginFragment);
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.authenticationActivity_frameLayout, fragment);

        fragmentTransaction.commit();
    }

    // Helpers
    private String getTokenFromMessage(JSONObject jsonObject) {
        String token = "";
        try {
            token = jsonObject.getString(Customer.tokenKey);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return token;
    }

    // Request functions
    public void registerCustomer(Customer customer) {
        postData.setEndPoint(Constant.registerCustomer);
        postData.setTaskType(Constant.register);
        postData.execute(Customer.toJSON(customer));
    }

    public void loginCustomer(String email, String password) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.email, email);
            jsonObject.put(Constant.password, password);

            postData.setEndPoint(Constant.loginCustomer);
            postData.setTaskType(Constant.login);
            postData.execute(jsonObject);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    // Start activity functions


    // Callback functions
    public void onRegisterFinished() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    public void onLoginFinished(String message) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(getTokenFromMessage(ApiData.getData(message)));
        getLocalFileController().writeFile(arrayList);
        Intent intent = new Intent(getContext(), MainActivity.class);
        ApiData<Customer> customerData = ApiData.fromJSON(ApiData.getData(message), Customer.class);
        intent.putExtra(Constant.customerKey, customerData.getData());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onFinished(String message, String taskType) {
        if (taskType.equals(Constant.register)) {
            onRegisterFinished();
        } else if (taskType.equals(Constant.login)) {
            onLoginFinished(message);
        }
    }
}
