package com.example.asm3.controllers;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.asm3.MainActivity;
import com.example.asm3.R;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.localStorage.LocalFileController;
import com.example.asm3.base.networking.api.ApiService;
import com.example.asm3.config.Constant;
import com.example.asm3.fragments.authenticationActivity.LoginFragment;
import com.example.asm3.fragments.authenticationActivity.RegisterFragment;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AuthenticationActivityController extends BaseController {
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private ApiService apiService;
    private LocalFileController localFileController;


    public AuthenticationActivityController(Context context, Activity activity) {
        super(context, activity);

        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        apiService = new ApiService(Constant.baseDomain);
        loginFragment.setController(this);
        registerFragment.setController(this);
        localFileController = new LocalFileController("token.txt", getContext());
    }

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

    public void registerCustomer(Customer customer) {
        RegisterCustomer registerCustomerTask = new RegisterCustomer();
        registerCustomerTask.execute(Customer.toJSON(customer));
    }

    public void loginCustomer(String username, String password) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.username, username);
            jsonObject.put(Constant.password, password);
            LoginCustomer loginCustomerTask = new LoginCustomer();
            loginCustomerTask.execute(jsonObject);
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    private class RegisterCustomer extends AsyncTask<JSONObject, String, String> {
        @Override
        protected String doInBackground(JSONObject... data) {
            String message = apiService.postJSON(Constant.registerCustomer, data[0]);
            return message;
        }

        @Override
        protected void onPostExecute(String message) {
            onFinished(message, Constant.register);
        }
    }

    private class LoginCustomer extends AsyncTask<JSONObject, String, String> {

        @Override
        protected String doInBackground(JSONObject... data) {
            String message = apiService.postJSON(Constant.loginCustomer, data[0]);
            return message;
        }

        @Override
        protected void onPostExecute(String message) {
            onFinished(message, Constant.login);
        }
    }

    public void onFinished(String message, String mainFragment) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            if (jsonObject.getBoolean("error")) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            } else {
                if (mainFragment.equals(Constant.register)) {
                    onRegisterFinished();
                } else if (mainFragment.equals(Constant.login)) {
                    onLoginFinished(message);
                }
            }
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRegisterFinished() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    public void onLoginFinished(String message) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(getToken(ApiData.getData(message)));
        localFileController.writeFile(arrayList);

        Intent intent = new Intent(getContext(), MainActivity.class);
        ApiData<Customer> customerData = ApiData.fromJSON(ApiData.getData(message), Customer.class);
        intent.putExtra("customerData", customerData.getData());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private String getToken(JSONObject jsonObject) {
        String token = "";
        try {
            token = jsonObject.getString("token");
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return token;
    }
}
