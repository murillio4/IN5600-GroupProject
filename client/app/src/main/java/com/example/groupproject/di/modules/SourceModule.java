package com.example.groupproject.di.modules;

import android.app.Application;

import com.example.groupproject.data.network.request.VolleyRequest;
import com.example.groupproject.data.sources.local.PersonLocalDataSource;
import com.example.groupproject.data.sources.remote.PersonRemoteDataSource;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SourceModule {
    @Provides
    @Singleton
    PersonRemoteDataSource providePersonRemoteDataSource(VolleyRequest volleyRequest) {
        return new PersonRemoteDataSource(volleyRequest);
    }

    @Provides
    @Singleton
    PersonLocalDataSource providePersonLocalDataSource(Application application, Gson gson) {
        return new PersonLocalDataSource(application.getApplicationContext(), gson);
    }
}
