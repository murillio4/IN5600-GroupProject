package com.example.groupproject.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.groupproject.R;
import com.example.groupproject.data.model.ClaimList;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.ui.adapter.ClaimListRecyclerViewAdapter;
import com.example.groupproject.ui.fragment.DropdownMenuFragment;
import com.example.groupproject.ui.viewModel.ClaimsViewModel;
import com.example.groupproject.ui.viewModel.LoginViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

public class MainActivity extends SessionActivity {

    private static final String TAG = "MainActivity";

    @Inject
    ClaimsViewModel claimsViewModel;

    @Inject
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initCreateNewClaimFloatingActionButton();

        Person loggedInPerson = loginViewModel.getLoggedInPerson();
        if (loggedInPerson != null) {
            fetchClaimsForPersonWithId(loggedInPerson.getId());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
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

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void openDropdownMenu() {
        FragmentManager fm = getSupportFragmentManager();
        new DropdownMenuFragment().showNow(fm);
    }

    private void initCreateNewClaimFloatingActionButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.create_new_claim_fab);
        floatingActionButton.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Create new claim", Toast.LENGTH_LONG).show());
    }

    private void initClaimListRecyclerView(ClaimList claimList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ClaimListRecyclerViewAdapter claimListRecyclerViewAdapter =
                new ClaimListRecyclerViewAdapter(this, claimList);
        recyclerView.setAdapter(claimListRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void fetchClaimsForPersonWithId(String id) {
        claimsViewModel.getClaims(id).observe(this, claimsResult -> {
            switch (claimsResult.getStatus()) {
                case LOADING:
                    Log.d(TAG, "onCreate: Loading resource");
                    break;
                case ERROR:
                    Log.d(TAG, "onCreate: Failed to fetch get claims");
                    break;
                case SUCCESS:
                    Log.d(TAG, "onCreate: Successfully fetched claims");
                    initClaimListRecyclerView(claimsResult.getData());
                    break;
                default:
                    Log.d(TAG, "onCreate: Unknown result");
                    break;
            }
        });
    }
}
