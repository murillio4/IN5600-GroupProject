package com.example.groupproject.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.ui.result.LocationResult;
import com.example.groupproject.ui.result.PhotoResult;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocationViewModel extends ViewModel {
    private MutableLiveData<LocationResult> locationResult = new MutableLiveData<>();

    @Inject
    public LocationViewModel() {}

    public LiveData<LocationResult> getLocationResult() {
        return locationResult;
    }

    public void setLocationResult(LocationResult locationResult) {
        this.locationResult.setValue(locationResult);
    }
}

