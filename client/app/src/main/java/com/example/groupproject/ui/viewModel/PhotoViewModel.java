package com.example.groupproject.ui.viewModel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.groupproject.ui.result.PhotoResult;

import javax.inject.Inject;

public class PhotoViewModel extends AndroidViewModel {
    private MutableLiveData<PhotoResult> photoResult = new MutableLiveData<>();

    @Inject
    public PhotoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<PhotoResult> getPhotoResult() {
        return photoResult;
    }

    public void setPhotoResult(PhotoResult photoResult) {
        this.photoResult.setValue(photoResult);
    }
}
