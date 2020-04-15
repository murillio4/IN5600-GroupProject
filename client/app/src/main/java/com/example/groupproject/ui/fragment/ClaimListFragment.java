package com.example.groupproject.ui.fragment;

import android.content.Context;
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

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ClaimListFragment extends DaggerFragment implements View.OnClickListener {

    private static final String TAG = "ClaimListFragment";

    @Inject
    ClaimsViewModel claimsViewModel;

    @Inject
    LoginViewModel loginViewModel;

    @Inject
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_claim_list, container, false);
        view.findViewById(R.id.create_new_claim_fab).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Person loggedInPerson = loginViewModel.getLoggedInPerson();
        if (loggedInPerson != null) {
            fetchClaimsForPersonWithId(loggedInPerson.getId());
        } else {
            Toast.makeText(context, "Unable to get logged in user", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_new_claim_fab:
                startCreateClaimFragment();
                break;
            default:
                break;
        }
    }

    private void startCreateClaimFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, new CreateClaimFragment())
                .addToBackStack(TAG)
                .commit();
    }

    private void initClaimListRecyclerView(ClaimList claimList) {
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new ClaimListRecyclerViewAdapter(getActivity(), claimList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
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
