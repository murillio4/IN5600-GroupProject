package com.example.groupproject.ui.viewModel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.ui.event.Event;
import com.example.groupproject.ui.result.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PhotoViewModel extends ViewModel {
    private MutableLiveData<Event<Result<Uri>>> photoResult = new MutableLiveData<>();

    @Inject
    public PhotoViewModel() {}

    public LiveData<Event<Result<Uri>>> getPhotoResult() {
        return photoResult;
    }

    public void setPhotoResult(Result<Uri> photoResult) {
        this.photoResult.setValue(new Event<>(photoResult));
    }
}
