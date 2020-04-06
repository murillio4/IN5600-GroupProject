package com.example.groupproject.di.modules.main;

import androidx.fragment.app.FragmentManager;

import com.example.groupproject.ui.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainMiscModule {
    @Provides
    @Singleton
    FragmentManager provideFragmentManager(MainActivity mainActivity) {
        return mainActivity.getSupportFragmentManager();
    }
}
