package com.example.groupproject.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject.R;
import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.ClaimList;
import com.example.groupproject.data.util.PermissionUtil;
import com.example.groupproject.data.util.TransitionUtil;
import com.example.groupproject.ui.adapter.ClaimListRecyclerViewAdapter;
import com.example.groupproject.ui.viewModel.ClaimsViewModel;
import com.example.groupproject.ui.viewModel.LoginViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ClaimListFragment extends DaggerFragment implements View.OnClickListener {

    public static final String TAG = "ClaimListFragment";

    @Inject
    ClaimsViewModel claimsViewModel;

    @Inject
    Context context;

    private ClaimList claimList = null;

    private String[] permissions = new String[] {
        Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_claim_list, container, false);
        view.findViewById(R.id.claim_list_create_claim_new_button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        claimsViewModel.getClaims().observe(getActivity(), claimListResult -> {
            if (claimListResult == null) {
                return;
            }

            if (claimListResult.getError() != null) {
                Log.d(TAG, "onViewCreated: Failed to fetch claims");
            } else if (claimListResult.getSuccess() != null) {
                claimList = claimListResult.getSuccess();
                requestPermissions(() -> initClaimListRecyclerView(claimListResult.getSuccess()));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.claim_list_create_claim_new_button) {
            if (claimList == null) {
                Toast.makeText(context, R.string.unexpected_error, Toast.LENGTH_SHORT).show();
            } else if (claimList.getNumberOfClaims() == Constants.Claim.CLAIM_MAX_COUNT) {
                Toast.makeText(context, R.string.claim_list_add_claim_max_error, Toast.LENGTH_SHORT).show();
            } else {
                TransitionUtil.toNextFragment(getActivity(), TAG, new CreateClaimFragment());
            }
        }
    }

    private void requestPermissions(Runnable callback) {
        PermissionUtil.requestPermissions(
                context,
                permissions,
                callback,
                dexterError -> Log.d(TAG, "requestPermissions: Failed to request permissions"));
    }

    private void initClaimListRecyclerView(ClaimList claimList) {
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new ClaimListRecyclerViewAdapter(getActivity(), claimList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
    }

}
