package com.example.groupproject.ui.login;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.app.Application;
import android.util.Patterns;

import com.example.groupproject.data.repositories.LoginRepository;
import com.example.groupproject.data.Result;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.R;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    private final Observer<Result<Person>> loginObserver = new Observer<Result<Person>>() {
        @Override
        public void onChanged(Result<Person> result) {
            if (result instanceof Result.Success) {
                Person data = ((Result.Success<Person>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getName())));
            }
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
        LiveData<Result<Person>> result = loginRepository.login(username, password);
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
        return password != null && password.trim().length() > 5;
    }

    public boolean isLoggedIn() {
        return loginRepository.isLoggedIn();
    }

    public Person getUser() {
        return loginRepository.getUser();
    }
}
