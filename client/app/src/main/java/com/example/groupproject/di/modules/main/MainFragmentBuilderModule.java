package com.example.groupproject.di.modules.main;
import com.example.groupproject.di.modules.claim.ClaimFragmentBuilderModule;
import com.example.groupproject.di.modules.claim.ClaimLocationModule;
import com.example.groupproject.di.modules.claim.ClaimViewModelModule;
import com.example.groupproject.ui.fragment.CreateClaimFragment;
import com.example.groupproject.ui.fragment.DisplayClaimFragment;
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

    @ContributesAndroidInjector(modules = {
            ClaimLocationModule.class,
            ClaimViewModelModule.class,
            ClaimFragmentBuilderModule.class
    })
    abstract DisplayClaimFragment contributeDisplayClaimFragment();

    @ContributesAndroidInjector(modules = {
            ClaimLocationModule.class,
            ClaimViewModelModule.class,
            ClaimFragmentBuilderModule.class
    })
    abstract CreateClaimFragment contributeCreateClaimFragment();
}
