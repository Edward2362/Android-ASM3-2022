package com.example.asm3.fragments.authenticationActivity;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuthViewModel extends ViewModel {
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<FragmentManager> fragmentManager = new MutableLiveData<>();
    private final MutableLiveData<LoginFragment> loginFragment = new MutableLiveData<>();
    private final MutableLiveData<RegisterFragment> registerFragment = new MutableLiveData<>();

    public AuthViewModel() {
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getPassword() {
        return password;
    }

    public LiveData<FragmentManager> getFragmentManager() {
        return fragmentManager;
    }

    public LiveData<LoginFragment> getLoginFragment() {
        return loginFragment;
    }

    public LiveData<RegisterFragment> getRegisterFragment() {
        return registerFragment;
    }

    public void setEmail(String newEmail) {
        email.setValue(newEmail);
    }

    public void setPassword(String newPassword) {
        password.setValue(newPassword);
    }

    public void setFragmentManager(FragmentManager newFragmentManager) {
        fragmentManager.setValue(newFragmentManager);
    }

    public void setLoginFragment(LoginFragment newLoginFragment) {
        loginFragment.setValue(newLoginFragment);
    }

    public void setRegisterFragment(RegisterFragment newRegisterFragment) {
        registerFragment.setValue(newRegisterFragment);
    }
}
