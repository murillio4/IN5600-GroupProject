package com.example.groupproject.ui.login;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.app.Application;
import android.util.Patterns;

import com.example.groupproject.data.repositories.LoginRepository;
import com.example.groupproject.data.network.model.Result;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    private final Observer<Result<Person>> loginObserver = result -> {
        if (result.getStatus() == Result.Status.SUCCESS) {
            if (result.getData() == null) {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            } else {
                Person data = result.getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getName())));
            }
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    };

    public LoginViewModel(Application application, LoginRepository loginRepository) {
        super(application);
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        LiveData<Result<Person>> result = loginRepository.login(username, md5(password));
        result.observeForever(loginObserver);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }


    public void logout() {
        loginRepository.logout();
    }

    private String md5(String password) {
        MessageDigest messageDigest;
        byte[] passwordHash;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            passwordHash = messageDigest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < passwordHash.length; i++) {
                String num = String.format("%02X", 0xFF & passwordHash[i]);
                hexString.append(num.toLowerCase());
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 3;
    }

    public boolean isLoggedIn() {
        return loginRepository.isLoggedIn();
    }

    public Person getUser() {
        return loginRepository.getUser();
    }
}
