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
import com.example.asm3.controllers.LoginFragmentController;
import com.example.asm3.controllers.RegisterFragmentController;
import com.example.asm3.models.Customer;

public class RegisterFragment extends Fragment {
    private RegisterFragmentController registerFragmentController;
    private AuthViewModel authViewModel;
//    private View view;

    public RegisterFragment() {
        // Required empty public constructor
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
        View register = inflater.inflate(R.layout.activity_authentication_fragment_register, container, false);
//        Button submitButton = (Button) view.findViewById(R.id.authenticationActivity_registerFragment_submit);
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onSubmit();
//            }
//        });
        onInit(register, authViewModel);
        return register;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerFragmentController.onResume();
    }

    public void onInit(View view, ViewModel viewModel) {
        registerFragmentController = new RegisterFragmentController(requireContext(), requireActivity(), view, viewModel);
        registerFragmentController.onInit();
    }

//    public void onSubmit() {
//        String email = "";
//        String password = "";
//        String username = "";
//        String address = "";
//        String role = "";
//        float rating = 0F;
//
//        EditText emailInput = (EditText) view.findViewById(R.id.authenticationActivity_registerFragment_emailInput);
//        EditText passwordInput = (EditText) view.findViewById(R.id.authenticationActivity_registerFragment_passwordInput);
//        EditText usernameInput = (EditText) view.findViewById(R.id.authenticationActivity_registerFragment_usernameInput);
//        EditText addressInput = (EditText) view.findViewById(R.id.authenticationActivity_registerFragment_addressInput);
//
//        email = String.valueOf(emailInput.getText());
//        password = String.valueOf(passwordInput.getText());
//        username = String.valueOf(usernameInput.getText());
//        address = String.valueOf(addressInput.getText());
//        role = Customer.customerRole;
//
//        Customer customer = new Customer(email, password, username, address, role, rating);
//
//        authenticationActivityController.registerCustomer(customer);
//    }
}
