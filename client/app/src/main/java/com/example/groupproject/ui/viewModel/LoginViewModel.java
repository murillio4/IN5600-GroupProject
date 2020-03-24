package com.example.groupproject.ui.viewModel;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.app.Application;
import android.util.Patterns;

import com.example.groupproject.data.Resource;
import com.example.groupproject.data.Status;
import com.example.groupproject.data.repositories.PersonRepository;
import com.example.groupproject.data.network.model.Result;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.R;
import com.example.groupproject.ui.view.LoggedInUserView;
import com.example.groupproject.ui.state.LoginFormState;
import com.example.groupproject.ui.result.LoginResult;

import javax.inject.Inject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.internal.observers.DisposableLambdaObserver;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private PersonRepository personRepository;

    @Inject
    public LoginViewModel(Application application, PersonRepository personRepository) {
        super(application);
        this.personRepository = personRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        personRepository.login(username, md5(password))
                .subscribe(personResource -> {
                    System.out.println(personResource.getStatus());

                    if (personResource.getStatus() == Status.LOADING) { return; }

                    switch (personResource.getStatus()) {
                        case ERROR:
                            loginResult.setValue(new LoginResult(R.string.login_failed));
                            break;
                        case SUCCESS:
                            if (personResource.getData() == null) {
                                loginResult.setValue(new LoginResult(R.string.login_failed));
                            } else {
                                Person data = personResource.getData();
                                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getName())));
                            }
                            break;
                    }
                });
                /*.subscribe(new DisposableObserver<Resource<Person>>() {
                    @Override
                    public void onNext(@NonNull Resource<Person> personResource) {

                        System.out.println(personResource.getStatus());

                        if (personResource.getStatus() == Status.LOADING) { return; }

                        switch (personResource.getStatus()) {
                            case ERROR:
                                loginResult.setValue(new LoginResult(R.string.login_failed));
                                break;
                            case SUCCESS:
                                if (personResource.getData() == null) {
                                    loginResult.setValue(new LoginResult(R.string.login_failed));
                                } else {
                                    Person data = personResource.getData();
                                    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getName())));
                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}

                    @Override
                    public void onComplete() {}
                });*/
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
        personRepository.logout();
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
        return personRepository.isLoggedIn();
    }

    public Person getUser() {
        return personRepository.getUser();
    }
}
