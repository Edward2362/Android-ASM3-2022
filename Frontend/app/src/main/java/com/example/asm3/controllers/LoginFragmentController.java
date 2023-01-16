package com.example.asm3.controllers;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.asm3.MainActivity;
import com.example.asm3.R;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.PostData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.fragments.authenticationActivity.AuthViewModel;
import com.example.asm3.fragments.authenticationActivity.LoginFragment;
import com.example.asm3.fragments.authenticationActivity.RegisterFragment;
import com.example.asm3.models.ApiData;
import com.example.asm3.models.Customer;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginFragmentController extends BaseController implements
        View.OnClickListener,
        AsyncTaskCallBack {
    // API
    private PostData postData;

    private View view;
    private FragmentManager fragmentManager;
    private RegisterFragment registerFragment;
    private TextInputEditText emailLoginEt, pwLoginEt;
    private TextView forgetPwTxt, registerNavTxt;
    private Button loginBtn;
    private int authLayoutId = R.id.authenticationActivity_fragmentContainerView;

    private AuthViewModel authViewModel;
    private LiveData<String> email;
    private LiveData<String> password;

    public LoginFragmentController(Context context, FragmentActivity activity, View view, ViewModel viewModel) {
        super(context, activity);
        this.view = view;
        this.authViewModel = (AuthViewModel) viewModel;

        email = authViewModel.getEmail();
        password = authViewModel.getPassword();
        fragmentManager = authViewModel.getFragmentManager().getValue();
        registerFragment = authViewModel.getRegisterFragment().getValue();
        postData = new PostData(getContext(), this);
    }

    // Render functions
    @Override
    public void onInit() {
        emailLoginEt = view.findViewById(R.id.emailLoginEt);
        pwLoginEt = view.findViewById(R.id.pwLoginEt);
        forgetPwTxt = view.findViewById(R.id.forgetPwTxt);
        registerNavTxt = view.findViewById(R.id.registerNavTxt);
        loginBtn = view.findViewById(R.id.loginBtn);

        forgetPwTxt.setOnClickListener(this);
        registerNavTxt.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    public void onResume() {
        emailLoginEt.setText(email.getValue());
        pwLoginEt.setText(password.getValue());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgetPwTxt:
                break;
            case R.id.registerNavTxt:
                Helper.loadFragment(fragmentManager, registerFragment, "register", authLayoutId);
                break;
            case R.id.loginBtn:
                onLogin();
                break;
        }
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

    public void onLogin() {
        String inputEmail = String.valueOf(emailLoginEt.getText());
        String inputPw = String.valueOf(pwLoginEt.getText());
        loginCustomer(inputEmail, inputPw);
    }

    // Request functions
    public void loginCustomer(String email, String password) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.email, email);
            jsonObject.put(Constant.password, password);

            postData.setEndPoint(Constant.loginCustomer);
            postData.setTaskType(Constant.login);
            postData.execute(jsonObject);
            Log.d(TAG, "loginCustomer: test normal");
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            Log.d(TAG, "loginCustomer: test error");
        }
    }


    // Navigation functions


    // Callback functions
    public void onLoginFinished(String message) {
        Log.d(TAG, "onLoginFinished: test " + message);
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
        Log.d(TAG, "onFinished: test " + message);
        if (taskType.equals(Constant.login)) {
            onLoginFinished(message);
        }
    }

    // Getter and Setter
}
