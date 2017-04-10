package com.yo.shishkoam.simpleui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yo.shishkoam.simpleui.R;
import com.yo.shishkoam.simpleui.helpers.Utils;
import com.yo.shishkoam.simpleui.managers.UserManager;

/**
 * Created by User on 08.04.2017
 */

public class RegistrationActivity extends AppCompatActivity {
    private EditText phoneView;
    private EditText countryCodeView;
    private EditText passwordView;
    private EditText passwordConfirmView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        phoneView = (EditText) findViewById(R.id.phone);
        countryCodeView = (EditText) findViewById(R.id.phone_country);
        passwordView = (EditText) findViewById(R.id.password);
        passwordConfirmView = (EditText) findViewById(R.id.repeat_password);
        View nextButton = findViewById(R.id.next_action);
        nextButton.setOnClickListener((view) -> attemptToRegister());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptToRegister() {

        // Reset errors.
        phoneView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String phone = countryCodeView.getText().toString() + phoneView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordCheck = passwordConfirmView.getText().toString();
        phone = phone.replaceAll("[\\-\\+ ]", "");

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            phoneView.setError(getString(R.string.error_field_required));
            focusView = phoneView;
            cancel = true;
        } else if (!Utils.validatePhoneNumber(phone)) {
            phoneView.setError(getString(R.string.error_invalid_email));
            focusView = phoneView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (!Utils.isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid repeat password.
        if (!cancel && !password.equals(passwordCheck)) {
            focusView = passwordView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            addUserAccess(phone, password);
        }
    }

    private void addUserAccess(String username, String password) {
        if (UserManager.INSTANCE.registrUser(username, password)) {
            Toast.makeText(this, R.string.register_success ,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.user_exist, Toast.LENGTH_SHORT).show();
        }
    }
}
