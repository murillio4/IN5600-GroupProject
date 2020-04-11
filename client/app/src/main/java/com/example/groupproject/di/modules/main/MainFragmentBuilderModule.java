package com.example.groupproject.di.modules.main;
import com.example.groupproject.ui.fragment.CreateClaimFragment;
import com.example.groupproject.ui.fragment.DisplayClaimFragment;
import com.example.groupproject.ui.fragment.ClaimListFragment;
import com.example.groupproject.ui.fragment.DropdownMenuFragment;
import com.example.groupproject.ui.fragment.LocationPickerDialogFragment;
import com.example.groupproject.ui.fragment.PhotoDialogFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract DropdownMenuFragment contributeDropdownMenuFragment();

    @ContributesAndroidInjector
    abstract ClaimListFragment contributeClaimListFragment();

    @ContributesAndroidInjector
    abstract DisplayClaimFragment contributeDisplayClaimFragment();

    @ContributesAndroidInjector
    abstract CreateClaimFragment contributeCreateClaimFragment();

    @ContributesAndroidInjector
    abstract PhotoDialogFragment contributePhotoDialogFragment();

    @ContributesAndroidInjector
    abstract LocationPickerDialogFragment contributeLocationPickerDialogFragment();
}
