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
import com.example.asm3.models.Customer;

public class RegisterFragment extends Fragment {
    private AuthenticationActivityController authenticationActivityController;
    private View view;

    public RegisterFragment() {
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
        view = inflater.inflate(R.layout.activity_authentication_fragment_register, container, false);
        Button submitButton = (Button) view.findViewById(R.id.authenticationActivity_registerFragment_submit);
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
        String firstName = "";
        String lastName = "";
        String address = "";
        String role = "";
        float rating = 0F;

        EditText usernameInput = (EditText) view.findViewById(R.id.authenticationActivity_registerFragment_usernameInput);
        EditText passwordInput = (EditText) view.findViewById(R.id.authenticationActivity_registerFragment_passwordInput);
        EditText firstNameInput = (EditText) view.findViewById(R.id.authenticationActivity_registerFragment_firstNameInput);
        EditText lastNameInput = (EditText) view.findViewById(R.id.authenticationActivity_registerFragment_lastNameInput);
        EditText addressInput = (EditText) view.findViewById(R.id.authenticationActivity_registerFragment_addressInput);

        username = String.valueOf(usernameInput.getText());
        password = String.valueOf(passwordInput.getText());
        firstName = String.valueOf(firstNameInput.getText());
        lastName = String.valueOf(lastNameInput.getText());
        address = String.valueOf(addressInput.getText());
        role = "CUSTOMER_ROLE";

        Customer customer = new Customer(username, password, firstName, lastName, address, role, rating);

        authenticationActivityController.registerCustomer(customer);
    }

}
