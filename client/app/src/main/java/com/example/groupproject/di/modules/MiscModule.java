package com.example.groupproject.di.modules;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MiscModule {
    @Singleton
    @Provides
    Context providesContext(Application application) {
        return application.getApplicationContext();
    }
}
