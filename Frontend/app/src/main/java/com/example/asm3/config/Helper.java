package com.example.asm3.config;
import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static String getQueryEndpoint(String key, String value) {
        return "?" + key + "=" + value;
    }

    public static void loadFragment(FragmentManager fragmentManager, Fragment fragment, String tag, int fragmentLayoutId) {
        fragmentManager.beginTransaction()
                .replace(fragmentLayoutId, fragment, tag)
                .setReorderingAllowed(true)
                .commit();
    }

    public static int inputChecked(TextInputEditText textInputEditText, TextInputLayout textInputLayout, @Nullable Pattern pattern, @Nullable String notification) {
        int checked = 1;
        String input = textInputEditText.getText().toString();
        textInputLayout.setErrorEnabled(false);
        if (input.equals("")) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Cannot be empty!");
            checked = 0;
        } else if (pattern != null) {
            Matcher matcher = pattern.matcher(input);
            if (!matcher.find()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(notification);
                checked = 0;
            }
        }
        return checked;
    }
}
