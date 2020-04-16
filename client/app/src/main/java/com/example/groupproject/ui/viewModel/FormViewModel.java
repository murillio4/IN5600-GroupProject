package com.example.groupproject.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.R;
import com.example.groupproject.ui.state.State;

import javax.inject.Inject;

public class FormViewModel extends ViewModel {

    private static final String TAG = "FormViewModel";

    private MutableLiveData<State<Integer>> descriptionState = new MutableLiveData<>();

    @Inject
    public FormViewModel() {}

    public void descriptionDataChanged(String description) {
        if(isValidDescription(description)) {
            descriptionState.setValue(new State<>(true));
        } else {
            descriptionState.setValue(new State<>(R.string.invalid_description));
        }
    }

    public LiveData<State<Integer>> getDescriptionState() {
        return descriptionState;
    }

    private boolean isValidDescription(String description) {
        if (description == null) {
            return false;
        }

        return !description.trim().isEmpty();
    }

}
