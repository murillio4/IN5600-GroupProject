package com.example.groupproject.di.modules;

import com.example.groupproject.di.modules.main.MainFragmentBuilderModule;
import com.example.groupproject.ui.activity.LoginActivity;
import com.example.groupproject.ui.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = {
            MainFragmentBuilderModule.class
    })
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract LoginActivity contributeLoginActivity();

}
