package com.example.groupproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.groupproject.ui.viewModel.SessionViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class SessionActivity extends DaggerAppCompatActivity {

    private static final String TAG = "SessionActivity";

    @Inject
    SessionViewModel sessionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sessionViewModel.getSession() == null) {
            Log.i(TAG, "onCreate: No user");
            startLoginActivity();
        } else {
            sessionViewModel.getSessionObserver().observe(this, session -> {
                if (session == null) {
                    Log.i(TAG, "onCreate: User observable");
                    startLoginActivity();
                }
            });
        }
    }

    private void startLoginActivity() {
        Log.d(TAG, "startLoginActivity");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
