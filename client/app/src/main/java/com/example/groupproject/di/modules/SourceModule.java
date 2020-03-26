package com.example.groupproject.di.modules;

import android.app.Application;

import com.example.groupproject.data.network.request.VolleyRequest;
import com.example.groupproject.data.sources.local.SessionLocalDataSource;
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
    SessionLocalDataSource providePersonLocalDataSource(Application application, Gson gson) {
        return new SessionLocalDataSource(application.getApplicationContext(), gson);
    }
}
