package com.example.groupproject.di.modules;

import android.content.Context;

import com.example.groupproject.data.network.request.VolleyRequest;
import com.example.groupproject.data.sources.local.ClaimsLocalDataSource;
import com.example.groupproject.data.sources.local.SessionLocalDataSource;
import com.example.groupproject.data.sources.remote.ClaimsRemoteDataSource;
import com.example.groupproject.data.sources.remote.SessionRemoteDataSource;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SourceModule {
    @Provides
    @Singleton
    SessionRemoteDataSource providePersonRemoteDataSource(VolleyRequest volleyRequest) {
        return new SessionRemoteDataSource(volleyRequest);
    }

    @Provides
    @Singleton
    SessionLocalDataSource providePersonLocalDataSource(Context context, Gson gson) {
        return new SessionLocalDataSource(context, gson);
    }

    @Provides
    @Singleton
    ClaimsRemoteDataSource provideClaimsRemoteDataSource(VolleyRequest volleyRequest) {
        return new ClaimsRemoteDataSource(volleyRequest);
    }

    @Provides
    @Singleton
    ClaimsLocalDataSource provideClaimsLocalDataSource(Context context, Gson gson) {
        return new ClaimsLocalDataSource(context, gson);
    }
}
