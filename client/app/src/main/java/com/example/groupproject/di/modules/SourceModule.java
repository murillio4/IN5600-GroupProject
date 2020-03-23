package com.example.groupproject.di.modules;

import com.android.volley.RequestQueue;
import com.example.groupproject.data.network.request.VolleyRequest;
import com.example.groupproject.data.sources.PersonRemoteDataSource;

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
}
