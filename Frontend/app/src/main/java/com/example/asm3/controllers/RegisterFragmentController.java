package com.example.asm3.controllers;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.example.asm3.R;
import com.example.asm3.base.controller.BaseController;
import com.example.asm3.base.networking.services.AsyncTaskCallBack;
import com.example.asm3.base.networking.services.PostData;
import com.example.asm3.config.Constant;
import com.example.asm3.config.Helper;
import com.example.asm3.fragments.authenticationActivity.AuthViewModel;
import com.example.asm3.fragments.authenticationActivity.LoginFragment;
import com.example.asm3.models.Customer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragmentController extends BaseController implements
        AsyncTaskCallBack,
        View.OnClickListener {
    // API
    private PostData postData;

    private View view;
    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private TextInputLayout emailRegisLayout, usernameRegisLayout, pwRegisLayout;
    private TextInputEditText emailRegisEt, usernameRegisEt, pwRegisEt;
    private TextView loginNavTxt;
    private Button registerBtn;
    private int authLayoutId = R.id.authenticationActivity_fragmentContainerView;

    private AuthViewModel authViewModel;
    private String email;
    private String password;

    public RegisterFragmentController(Context context, FragmentActivity activity, View view, ViewModel viewModel) {
        super(context, activity);
        this.view = view;
        this.authViewModel = (AuthViewModel) viewModel;

        fragmentManager = authViewModel.getFragmentManager().getValue();
        loginFragment = authViewModel.getLoginFragment().getValue();

    }

    // Render functions
    @Override
    public void onInit() {
        emailRegisLayout = view.findViewById(R.id.emailRegisLayout);
        usernameRegisLayout = view.findViewById(R.id.usernameRegisLayout);
        pwRegisLayout = view.findViewById(R.id.pwRegisLayout);
        emailRegisEt = view.findViewById(R.id.emailRegisEt);
        usernameRegisEt = view.findViewById(R.id.usernameRegisEt);
        pwRegisEt = view.findViewById(R.id.pwRegisEt);
        loginNavTxt = view.findViewById(R.id.loginNavTxt);
        registerBtn = view.findViewById(R.id.registerBtn);

        loginNavTxt.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    public void onResume() {
        emailRegisEt.setText("");
        usernameRegisEt.setText("");
        pwRegisEt.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginNavTxt:
                Helper.loadFragment(fragmentManager, loginFragment, "login", authLayoutId);
                break;
            case R.id.registerBtn:
                if (!isOnline()) {
                    showConnectDialog();
                    return;
                }
                onRegister();
                break;
        }
    }

    // Helpers
    public void onRegister() {
        email = String.valueOf(emailRegisEt.getText());
        String inputUsername = String.valueOf(usernameRegisEt.getText());
        password = String.valueOf(pwRegisEt.getText());

        if (validated()) {
            Customer newCustomer = new Customer();
            newCustomer.setEmail(email);
            newCustomer.setPassword(password);
            newCustomer.setUsername(inputUsername);
            registerCustomer(newCustomer);
        }
    }

    private boolean validated() {
        return Helper.inputChecked(emailRegisEt, emailRegisLayout, Constant.emailPattern, Constant.emailError) +
                Helper.inputChecked(usernameRegisEt, usernameRegisLayout, null, null) +
                Helper.inputChecked(pwRegisEt, pwRegisLayout, Constant.pwPattern, Constant.pwError)
                == 3;
    }

    // Request functions
    public void registerCustomer(Customer customer) {
        postData = new PostData(getContext(), this);
        postData.setEndPoint(Constant.registerCustomer);
        postData.setTaskType(Constant.register);
        postData.execute(Customer.toJSON(customer));
    }

    // Navigation functions


    // Callback functions
    public void onRegisterFinished(String message) {
        Helper.loadFragment(fragmentManager, loginFragment, "login", authLayoutId);
        authViewModel.setEmail(email);
        authViewModel.setPassword(password);
    }

    @Override
    public void onFinished(String message, String taskType) {
        if (taskType.equals(Constant.register)) {
            onRegisterFinished(message);
        }
    }

    @Override
    public void onError(String taskType) {
        if (taskType.equals(Constant.register)) {
            emailRegisLayout.setErrorEnabled(true);
            emailRegisLayout.setError("Email have already used!");
        }
    }

    // Getter and Setter

}
