package com.example.groupproject.di.components;

import android.app.Application;

import com.example.groupproject.AppController;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

//        (modules = {
//        ApiModule.class,
//        DbModule.class,
//        ViewModelModule.class,
//        ActivityModule.class,
//        AndroidSupportInjectionModule.class})

@Component(modules = {AndroidSupportInjectionModule.class})
@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(AppController appController);
}