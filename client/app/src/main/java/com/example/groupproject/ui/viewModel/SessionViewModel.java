package com.example.groupproject.ui.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.data.model.Person;
import com.example.groupproject.data.repositories.SessionRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.Disposable;

public class SessionViewModel extends ViewModel {
    private static final String TAG = "SessionViewModel";

    private MutableLiveData<Person> sessionState = new MutableLiveData<>();
    private SessionRepository sessionRepository;

    private Disposable sessionObserver;

    @Inject
    public SessionViewModel(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;

        sessionObserver = this.sessionRepository.getSessionObserver().subscribe(session -> {
            Log.i(TAG, "SessionViewModel: User added");
            sessionState.setValue(session);
        }, throwable -> {
            Log.i(TAG, "SessionViewModel: " + this);
            Log.i(TAG, "SessionViewModel: " + throwable.toString());
            sessionState.setValue(null);
        });
    }

    @Override
    protected void onCleared() {
        Log.i(TAG, "onCleared");
        sessionObserver.dispose();
    }

    public Person getSession() {
        return sessionRepository.getSession();
    }

    public LiveData<Person> getSessionObserver() {
        return sessionState;
    }

    public void removeSession() {
        sessionRepository.logout();
    }
}
