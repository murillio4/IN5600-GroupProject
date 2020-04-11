package com.example.groupproject.ui.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.data.Resource;
import com.example.groupproject.data.Status;
import com.example.groupproject.data.model.ClaimList;
import com.example.groupproject.data.repositories.ClaimsRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.observers.DisposableObserver;

public class ClaimsViewModel extends ViewModel {

    private MutableLiveData<Resource<ClaimList>> claimsResult = new MutableLiveData<>();
    // Inject?
    private ClaimsRepository claimsRepository;

    @Inject
    public ClaimsViewModel(ClaimsRepository claimsRepository) {
        this.claimsRepository = claimsRepository;
    }

    public LiveData<Resource<ClaimList>> getClaims(String id) {
        claimsRepository.getClaims(id)
                .subscribe(new DisposableObserver<Resource<ClaimList>>() {
                    @Override
                    public void onNext(@NonNull Resource<ClaimList> claimsResource) {
                        claimsResult.setValue(claimsResource);
                        if (claimsResource.getStatus() != Status.LOADING) {
                            dispose();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}

                    @Override
                    public void onComplete() {}
                });

        return claimsResult;
    }

}
