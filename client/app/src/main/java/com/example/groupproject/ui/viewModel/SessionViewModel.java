package com.example.groupproject.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.data.model.Person;
import com.example.groupproject.data.repositories.SessionRepository;

import javax.inject.Inject;

public class SessionViewModel extends ViewModel {

    private MutableLiveData<Person> sessionState = new MutableLiveData<>();

    private SessionRepository sessionRepository;

    @Inject
    public SessionViewModel(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;

        this.sessionRepository.observeSession().subscribe(session -> {
            sessionState.setValue(session);
        }, throwable -> {
            sessionState.setValue(null);
        });
    }

    public Person getSession() {
        return sessionRepository.getSession();
    }

    public LiveData<Person> getSessionObserver() {
        return sessionState;
    }
}
