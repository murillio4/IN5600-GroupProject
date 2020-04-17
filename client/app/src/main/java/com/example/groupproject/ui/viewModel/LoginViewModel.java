package com.example.groupproject.ui.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.data.Resource;
import com.example.groupproject.data.repositories.SessionRepository;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.R;
import com.example.groupproject.data.util.HashUtil;
import com.example.groupproject.ui.view.LoggedInUserView;
import com.example.groupproject.ui.result.LoginResult;

import javax.inject.Inject;

import io.reactivex.rxjava3.observers.DisposableObserver;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private SessionRepository sessionRepository;

    @Inject
    public LoginViewModel(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        sessionRepository.login(username, HashUtil.md5(password))
                .subscribe(buildLoginDisposableObserver());
    }

    public void logout() {
        sessionRepository.logout();
    }

    private DisposableObserver<Resource<Person>> buildLoginDisposableObserver() {
        return new DisposableObserver<Resource<Person>>() {
            @Override
            public void onNext(@NonNull Resource<Person> personResource) {
                switch (personResource.getStatus()) {
                    case LOADING:
                        break;
                    case ERROR:
                        loginResult.setValue(new LoginResult(R.string.login_failed));
                        dispose();
                        break;
                    case SUCCESS:
                        if (personResource.getData() == null) {
                            loginResult.setValue(new LoginResult(R.string.login_failed));
                        } else {
                            Person data = personResource.getData();
                            loginResult.setValue(new LoginResult(
                                    new LoggedInUserView(data.getName())));
                        }
                        dispose();
                        break;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {}

            @Override
            public void onComplete() {}
        };
    }
}
