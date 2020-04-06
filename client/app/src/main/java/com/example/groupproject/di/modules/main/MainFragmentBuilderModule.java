package com.example.groupproject.di.modules.main;
import com.example.groupproject.ui.fragment.ClaimListFragment;
import com.example.groupproject.ui.fragment.DropdownMenuFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract DropdownMenuFragment contributeDropdownMenuFragment();

    @ContributesAndroidInjector
    abstract ClaimListFragment contributeClaimListFragment();

}
