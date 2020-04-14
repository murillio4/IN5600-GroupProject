package com.example.groupproject.di.modules.claim;

import com.example.groupproject.ui.fragment.LocationPickerDialogFragment;
import com.example.groupproject.ui.fragment.PhotoDialogFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ClaimFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract PhotoDialogFragment contributePhotoDialogFragment();

    @ContributesAndroidInjector
    abstract LocationPickerDialogFragment contributeLocationPickerDialogFragment();
}

