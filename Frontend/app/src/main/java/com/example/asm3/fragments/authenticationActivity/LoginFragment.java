package com.example.asm3.fragments.authenticationActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.asm3.R;
import com.example.asm3.controllers.AuthenticationActivityController;
import com.example.asm3.controllers.HomeFragmentController;
import com.example.asm3.controllers.LoginFragmentController;

public class LoginFragment extends Fragment {
    private LoginFragmentController loginFragmentController;
    private AuthViewModel authViewModel;
//    View view;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View login = inflater.inflate(R.layout.activity_authentication_fragment_login, container, false);
//        Button submitButton = (Button) view.findViewById(R.id.authenticationActivity_loginFragment_submit);
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onSubmit();
//            }
//        });
        onInit(login, authViewModel);
        return login;
    }

    @Override
    public void onResume() {
        super.onResume();
        loginFragmentController.onResume();
    }

    public void onInit(View view, ViewModel viewModel) {
        loginFragmentController = new LoginFragmentController(requireContext(), requireActivity(), view, viewModel);
        loginFragmentController.onInit();
    }


//    public void setController(AuthenticationActivityController authenticationActivityController) {
//        this.authenticationActivityController = authenticationActivityController;
//    }


//    public void onSubmit() {
//        String email = "";
//        String password = "";
//
//        EditText usernameInput = (EditText) view.findViewById(R.id.authenticationActivity_loginFragment_emailInput);
//        EditText passwordInput = (EditText) view.findViewById(R.id.authenticationActivity_loginFragment_passwordInput);
//
//        email = String.valueOf(usernameInput.getText());
//        password = String.valueOf(passwordInput.getText());
//
//        authenticationActivityController.loginCustomer(email, password);
//    }
}