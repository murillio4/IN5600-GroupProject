package com.example.groupproject.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject.R;
import com.example.groupproject.data.model.ClaimList;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.ui.adapter.ClaimListRecyclerViewAdapter;
import com.example.groupproject.ui.viewModel.ClaimsViewModel;
import com.example.groupproject.ui.viewModel.LoginViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ClaimListFragment extends DaggerFragment {

    private static final String TAG = "ClaimListFragment";

    @Inject
    ClaimsViewModel claimsViewModel;

    @Inject
    LoginViewModel loginViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_claim_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initCreateNewClaimFloatingActionButton();

        Person loggedInPerson = loginViewModel.getLoggedInPerson();
        if (loggedInPerson != null) {
            fetchClaimsForPersonWithId(loggedInPerson.getId());
        }
    }

    private void initCreateNewClaimFloatingActionButton() {
        FloatingActionButton floatingActionButton =
                getView().findViewById(R.id.create_new_claim_fab);
        floatingActionButton.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Create new claim", Toast.LENGTH_LONG).show());
    }

    private void initClaimListRecyclerView(ClaimList claimList) {
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        ClaimListRecyclerViewAdapter claimListRecyclerViewAdapter =
                new ClaimListRecyclerViewAdapter(getActivity(), claimList);
        recyclerView.setAdapter(claimListRecyclerViewAdapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    private void fetchClaimsForPersonWithId(String id) {
        claimsViewModel.getClaims(id).observe(getActivity(), claimsResult -> {
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
