package com.example.asm3.controllers;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.asm3.R;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.config.Helper;
import com.example.asm3.custom.components.TopBarView;
import com.example.asm3.fragments.authenticationActivity.AuthViewModel;
import com.example.asm3.fragments.authenticationActivity.LoginFragment;
import com.example.asm3.fragments.authenticationActivity.RegisterFragment;
import com.example.asm3.models.Customer;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticationActivityController extends BaseController implements
        View.OnClickListener {

    // auth activity view
    private TopBarView topBar;
    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private Button backBtn;
    private int authLayoutId = R.id.authenticationActivity_fragmentContainerView;

    private AuthViewModel authViewModel;

    public AuthenticationActivityController(Context context, FragmentActivity activity) {
        super(context, activity);
        authViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);
        topBar = getActivity().findViewById(R.id.authTopBar);
        fragmentManager = getActivity().getSupportFragmentManager();
        topBar.setAuthPage();

        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();

        authViewModel.setFragmentManager(fragmentManager);
        authViewModel.setLoginFragment(loginFragment);
        authViewModel.setRegisterFragment(registerFragment);
    }

    // Render functions
    @Override
    public void onInit() {
        backBtn = topBar.getBackButton();
        backBtn.setOnClickListener(this);
        Helper.loadFragment(fragmentManager, loginFragment, "login", authLayoutId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                getActivity().finish();
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

    // Request functions


    // Navigation functions


    // Callback functions


    // Getter and Setter
    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public LoginFragment getLoginFragment() {
        return loginFragment;
    }

    public int getAuthLayoutId() {
        return authLayoutId;
    }
}
