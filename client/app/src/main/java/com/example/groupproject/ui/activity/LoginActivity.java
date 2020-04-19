package com.example.groupproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupproject.R;
import com.example.groupproject.ui.viewModel.FormViewModel;
import com.example.groupproject.ui.viewModel.LoginViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class LoginActivity extends DaggerAppCompatActivity
        implements TextWatcher, TextView.OnEditorActionListener, View.OnClickListener {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;

    @Inject
    LoginViewModel loginViewModel;

    @Inject
    FormViewModel formViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        usernameEditText.addTextChangedListener(this);
        passwordEditText.addTextChangedListener(this);
        passwordEditText.setOnEditorActionListener(this);
        loginButton.setOnClickListener(this);

        formViewModel.getUsernameState().observe(this, usernameState -> {
            if (usernameState != null) {
                loginButton.setEnabled(usernameState.isDataValid());
                if (usernameState.getData() != null) {
                    usernameEditText.setError(getString(usernameState.getData()));
                }
            }
        });

        formViewModel.getPasswordState().observe(this, passwordState -> {
            if (passwordState != null) {
                loginButton.setEnabled(passwordState.isDataValid());
                if (passwordState.getData() != null) {
                    passwordEditText.setError(getString(passwordState.getData()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                showLoginSuccess(loginResult.getSuccess());
                startMainActivity();
            }
        });
    }

    @Override
    public void onBackPressed() { /* Prevent user from going back */ }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        formViewModel.usernameDataChanged(usernameEditText.getText().toString());
        formViewModel.passwordDataChanged(passwordEditText.getText().toString());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                break;
            default:
                break;
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showLoginSuccess(@NonNull String displayName) {
        String welcome = getString(R.string.welcome) + displayName;
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_SHORT).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
