package com.example.groupproject.ui.viewModel;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.R;
import com.example.groupproject.data.State;

import javax.inject.Inject;

public class FormViewModel extends ViewModel {

    private static final String TAG = "FormViewModel";

    private MutableLiveData<State<Integer>> usernameState = new MutableLiveData<>();
    private MutableLiveData<State<Integer>> passwordState = new MutableLiveData<>();
    private MutableLiveData<State<Integer>> descriptionState = new MutableLiveData<>();

    @Inject
    public FormViewModel() {}

    public void usernameDataChanged(String username) {
        if (isValidUsername(username)) {
            usernameState.setValue(new State<>(true));
        } else {
            usernameState.setValue(new State<>(R.string.invalid_username));
        }
    }

    public void passwordDataChanged(String password) {
        if (isValidPassword(password)) {
            passwordState.setValue(new State<>(true));
        } else {
            passwordState.setValue(new State<>(R.string.invalid_password));
        }
    }

    public void descriptionDataChanged(String description) {
        if (isValidDescription(description)) {
            descriptionState.setValue(new State<>(true));
        } else {
            descriptionState.setValue(new State<>(R.string.invalid_description));
        }
    }

    public LiveData<State<Integer>> getUsernameState() {
        return usernameState;
    }

    public LiveData<State<Integer>> getPasswordState() {
        return passwordState;
    }

    public LiveData<State<Integer>> getDescriptionState() {
        return descriptionState;
    }

    private boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }

        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isValidPassword(String password) {
        return password != null && password.trim().length() > 3;
    }

    private boolean isValidDescription(String description) {
        if (description == null) {
            return false;
        }

        return !description.trim().isEmpty();
    }

}
