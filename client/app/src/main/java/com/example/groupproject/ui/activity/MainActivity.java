package com.example.groupproject.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.groupproject.R;
import com.example.groupproject.ui.fragment.ClaimListFragment;
import com.example.groupproject.ui.fragment.DropdownMenuFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SessionActivity {

    private static final String TAG = "MainActivity";

    final int REQUEST_SINGLE = 0;
    final int REQUEST_MULTIPLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        int amountOfRequestedPermissions = requestPermissions(new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE
        });

        if (amountOfRequestedPermissions == 0) {
            initClaimListFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SINGLE: {
                Log.d(TAG, "onRequestPermissionsResult: REQUEST_SINGLE");
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initClaimListFragment();
                } else {
                    // Disable the functionality
                }
                break;
            }
            case REQUEST_MULTIPLE:
                Log.d(TAG, "onRequestPermissionsResult: REQUEST_MULTIPLE");
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_menu:
                openDropdownMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int requestPermissions(String[] permissions) {
        List<String> neededPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (needPermission(permission)) {
                neededPermissions.add(permission);
            }
        }

        int amountOfPermissionsNeeded = neededPermissions.size();
        if (amountOfPermissionsNeeded > 0) {
            ActivityCompat.requestPermissions(this,
                    neededPermissions.toArray(new String[amountOfPermissionsNeeded]),
                    amountOfPermissionsNeeded == 1 ? REQUEST_SINGLE : REQUEST_MULTIPLE);
        }

        return amountOfPermissionsNeeded;
    }

    private boolean needPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return false; // Should ask for permission again after message to user
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private void openDropdownMenu() {
        FragmentManager fm = getSupportFragmentManager();
        new DropdownMenuFragment().showNow(fm);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initClaimListFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, new ClaimListFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
