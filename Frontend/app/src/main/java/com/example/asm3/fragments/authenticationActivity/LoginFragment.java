package com.example.asm3.fragments.authenticationActivity;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.asm3.R;
import com.example.asm3.controllers.AuthenticationActivityController;

public class LoginFragment extends Fragment {
    private AuthenticationActivityController authenticationActivityController;
    View view;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_authentication_fragment_login, container, false);

        Button submitButton = (Button) view.findViewById(R.id.authenticationActivity_loginFragment_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });

        return view;
    }


    public void setController(AuthenticationActivityController authenticationActivityController) {
        this.authenticationActivityController = authenticationActivityController;
    }


    public void onSubmit() {
        String username = "";
        String password = "";

        EditText usernameInput = (EditText) view.findViewById(R.id.authenticationActivity_loginFragment_usernameInput);
        EditText passwordInput = (EditText) view.findViewById(R.id.authenticationActivity_loginFragment_passwordInput);

        username = String.valueOf(usernameInput.getText());
        password = String.valueOf(passwordInput.getText());

        authenticationActivityController.loginCustomer(username, password);
    }
}
