package com.example.groupproject.ui.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.data.Result;
import com.example.groupproject.data.network.model.Resource;
import com.example.groupproject.data.repositories.SessionRepository;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.R;
import com.example.groupproject.data.util.HashUtil;
import javax.inject.Inject;

import io.reactivex.rxjava3.observers.DisposableObserver;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<Result<String>> loginResult = new MutableLiveData<>();
    private SessionRepository sessionRepository;

    @Inject
    public LoginViewModel(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public LiveData<Result<String>> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        sessionRepository.login(username, HashUtil.md5(password))
                .subscribe(buildLoginDisposableObserver());
    }

    private DisposableObserver<Resource<Person>> buildLoginDisposableObserver() {
        return new DisposableObserver<Resource<Person>>() {
            @Override
            public void onNext(@NonNull Resource<Person> personResource) {
                switch (personResource.getStatus()) {
                    case LOADING:
                        break;
                    case ERROR:
                        loginResult.setValue(Result.error(R.string.login_failed));
                        dispose();
                        break;
                    case SUCCESS:
                        if (personResource.getData() == null) {
                            loginResult.setValue(Result.error(R.string.login_failed));
                        } else {
                            Person data = personResource.getData();
                            loginResult.setValue(Result.success(data.getName()));
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
