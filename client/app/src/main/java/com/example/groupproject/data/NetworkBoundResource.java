package com.example.groupproject.data;


import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class NetworkBoundResource<ResultType, RequestType> {
    private Observable<Resource<ResultType>> result;

    @MainThread
    protected NetworkBoundResource() {
        Observable<Resource<ResultType>> source;
        if (shouldFetchRemote()) {
            source = fetchRemote()
                    .subscribeOn(Schedulers.io())
                    .doOnNext(remoteResponse -> saveRemoteResult(processResponse(remoteResponse)))
                    .flatMap(remoteResponse -> fetchLocal().toObservable().map(Resource::success))
                    .doOnError(this::onRemoteFetchError)
                    .onErrorResumeNext(t -> fetchLocal()
                            .toObservable()
                            .map(data -> Resource.error(t, data))
                            .defaultIfEmpty(Resource.error(t, null)))
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            source = fetchLocal()
                    .toObservable()
                    .map(Resource::success)
                    .defaultIfEmpty(Resource.error(new Throwable(), null));
        }

        result = Observable.concat(
                fetchLocal()
                    .toObservable()
                    .map(Resource::loading)
                    .defaultIfEmpty(Resource.loading(null)),
                source
        );
    }

    public Observable<Resource<ResultType>> getAsObservable() {return result;}

    @MainThread
    protected abstract boolean shouldFetchRemote();

    protected void onRemoteFetchError(Throwable t) {
    }

    @WorkerThread
    protected RequestType processResponse(Resource<RequestType> response) {return response.getData();}

    @WorkerThread
    protected abstract void saveRemoteResult(@NonNull RequestType item);

    @NonNull
    @MainThread
    protected abstract Flowable<ResultType> fetchLocal();

    @NonNull
    @MainThread
    protected abstract Observable<Resource<RequestType>> fetchRemote();
}