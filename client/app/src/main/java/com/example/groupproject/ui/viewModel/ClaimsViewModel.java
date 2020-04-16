package com.example.groupproject.ui.viewModel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.data.Resource;
import com.example.groupproject.data.Status;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.model.ClaimList;
import com.example.groupproject.data.repositories.ClaimsRepository;
import com.example.groupproject.data.repositories.SessionRepository;
import com.example.groupproject.data.util.MapUtil;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import io.reactivex.rxjava3.observers.DisposableObserver;

public class ClaimsViewModel extends ViewModel {

    private MutableLiveData<Resource<ClaimList>> claimsResult = new MutableLiveData<>();
    private MutableLiveData<Resource<String>> createClaimResult = new MutableLiveData<>();
    private MutableLiveData<Resource<String>> updateClaimResult = new MutableLiveData<>();

    private ClaimsRepository claimsRepository;
    private SessionRepository sessionRepository;

    @Inject
    public ClaimsViewModel(ClaimsRepository claimsRepository, SessionRepository sessionRepository) {
        this.claimsRepository = claimsRepository;
        this.sessionRepository = sessionRepository;
    }

    public void clear() {
        claimsRepository.clear();
    }

    public String getNextClaimId() {
        return claimsRepository.getNextClaimId();
    }

    public LiveData<Resource<ClaimList>> getClaims() {
        String id = sessionRepository.getSession().getId();
        claimsRepository.getClaims(id).subscribe(buildGetClaimsDisposableObserver());
        return claimsResult;
    }

    public LiveData<Resource<String>> createClaim(Claim claim) {
        String userId = sessionRepository.getSession().getId();
        claimsRepository.createClaim(userId, claim).subscribe(buildCrateClaimDisposableObserver());
        return createClaimResult;
    }

    public LiveData<Resource<String>> updateClaim(Claim claim) {
        String userId = sessionRepository.getSession().getId();
        claimsRepository.updateClaim(userId, claim).subscribe(buildUpdateClaimDisposableObserver());
        return updateClaimResult;
    }

    private DisposableObserver<Resource<String>> buildCrateClaimDisposableObserver() {
       return new DisposableObserver<Resource<String>>() {
           @Override
           public void onNext(@NonNull Resource<String> stringResource) {
               createClaimResult.setValue(stringResource);
               if (stringResource.getStatus() != Status.LOADING) {
                   dispose();
               }
           }

           @Override
           public void onError(@NonNull Throwable e) { }

           @Override
           public void onComplete() { }
       };
    }

    private DisposableObserver<Resource<String>> buildUpdateClaimDisposableObserver() {
        return new DisposableObserver<Resource<String>>() {
            @Override
            public void onNext(@NonNull Resource<String> stringResource) {
                updateClaimResult.setValue(stringResource);
                if (stringResource.getStatus() != Status.LOADING) {
                    dispose();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) { }

            @Override
            public void onComplete() { }
        };
    }

    private DisposableObserver<Resource<ClaimList>> buildGetClaimsDisposableObserver() {
        return new DisposableObserver<Resource<ClaimList>>() {
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
        };
    }

}
