package com.example.groupproject.ui.activity;

import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.groupproject.R;
import com.example.groupproject.ui.fragment.ClaimListFragment;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        initClaimListFragment();
    }

    private void initClaimListFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, new ClaimListFragment());
        fragmentTransaction.commit();
    }

}
