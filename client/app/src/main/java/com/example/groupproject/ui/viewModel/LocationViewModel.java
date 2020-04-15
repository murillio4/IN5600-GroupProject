package com.example.groupproject.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.ui.event.Event;
import com.example.groupproject.ui.result.Result;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocationViewModel extends ViewModel {
    private MutableLiveData<Event<Result<LatLng>>> locationResult = new MutableLiveData<>();

    @Inject
    public LocationViewModel() {}

    public LiveData<Event<Result<LatLng>>> getLocationResult() {
        return locationResult;
    }

    public void setLocationResult(Result<LatLng> locationResult) {
        this.locationResult.setValue(new Event<>(locationResult));
    }
}

