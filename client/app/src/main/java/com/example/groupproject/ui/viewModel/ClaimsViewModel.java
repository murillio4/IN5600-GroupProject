package com.example.groupproject.ui.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groupproject.R;
import com.example.groupproject.data.Result;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.data.network.model.Resource;
import com.example.groupproject.data.network.model.Status;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.model.ClaimList;
import com.example.groupproject.data.repositories.ClaimsRepository;
import com.example.groupproject.data.repositories.SessionRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.observers.DisposableObserver;

public class ClaimsViewModel extends ViewModel {

    private MutableLiveData<Result<ClaimList>> claimsResult = new MutableLiveData<>();
    private MutableLiveData<Result<String>> createClaimResult = new MutableLiveData<>();
    private MutableLiveData<Result<String>> updateClaimResult = new MutableLiveData<>();

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

    public LiveData<Result<ClaimList>> getClaims() {
        String id = sessionRepository.getSession().getId();
        claimsRepository.getClaims(id).subscribe(buildGetClaimsDisposableObserver());
        return claimsResult;
    }

    public LiveData<Result<String>> createClaim(Claim claim) {
        String userId = sessionRepository.getSession().getId();
        claimsRepository.createClaim(userId, claim).subscribe(buildCrateClaimDisposableObserver());
        return createClaimResult;
    }

    public LiveData<Result<String>> updateClaim(Claim claim) {
        String userId = sessionRepository.getSession().getId();
        claimsRepository.updateClaim(userId, claim).subscribe(buildUpdateClaimDisposableObserver());
        return updateClaimResult;
    }

    private DisposableObserver<Resource<String>> buildCrateClaimDisposableObserver() {
       return new DisposableObserver<Resource<String>>() {
           @Override
           public void onNext(@NonNull Resource<String> stringResource) {
               switch (stringResource.getStatus()) {
                   case LOADING:
                       break;
                   case ERROR:
                       createClaimResult.setValue(Result.error(R.string.create_claim_failed));
                       dispose();
                       break;
                   case SUCCESS:
                       if (stringResource.getData() == null) {
                           createClaimResult.setValue(Result.error(R.string.create_claim_failed));
                       } else {
                           createClaimResult.setValue(Result.success(stringResource.getData()));
                       }
                       dispose();
                       break;
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
                switch (stringResource.getStatus()) {
                    case LOADING:
                        break;
                    case ERROR:
                        updateClaimResult.setValue(Result.error(R.string.update_claim_failed));
                        dispose();
                        break;
                    case SUCCESS:
                        if (stringResource.getData() == null) {
                            updateClaimResult.setValue(Result.error(R.string.update_claim_failed));
                        } else {
                            updateClaimResult.setValue(Result.success(stringResource.getData()));
                        }
                        dispose();
                        break;
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
                switch (claimsResource.getStatus()) {
                    case LOADING:
                        break;
                    case ERROR:
                        claimsResult.setValue(Result.error(R.string.get_claims_failed));
                        dispose();
                        break;
                    case SUCCESS:
                        if (claimsResource.getData() == null) {
                            claimsResult.setValue(Result.error(R.string.get_claims_failed));
                        } else {
                            claimsResult.setValue(Result.success(claimsResource.getData()));
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
