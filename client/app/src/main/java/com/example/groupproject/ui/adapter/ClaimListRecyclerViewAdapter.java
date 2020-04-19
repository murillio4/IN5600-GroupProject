package com.example.groupproject.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject.R;
import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.model.ClaimList;
import com.example.groupproject.data.util.TransitionUtil;
import com.example.groupproject.ui.fragment.ClaimListFragment;
import com.example.groupproject.ui.fragment.DisplayClaimFragment;

public class ClaimListRecyclerViewAdapter extends RecyclerView.Adapter<ClaimListItemViewHolder> {

    private static final String TAG = "ClaimListRecyclerViewAdapter";

    private FragmentActivity context;
    private ClaimList claimList;

    public ClaimListRecyclerViewAdapter(FragmentActivity context, ClaimList claimList) {
        this.context = context;
        this.claimList = claimList;
    }

    @NonNull
    @Override
    public ClaimListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClaimListItemViewHolder(LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.layout_claim_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimListItemViewHolder holder, int position) {
        Claim claim = claimList.getClaims().get(position);

        holder.setListItemImage(context, claim.getPhotoPath())
                .setListItemText("Claim " + claim.getId())
                .setParentLayoutOnClickListner(v -> startDisplayClaimFragment(claim));
    }

    @Override
    public int getItemCount() {
        return claimList.getClaims().size();
    }

    private void startDisplayClaimFragment(Claim claim) {
        TransitionUtil.toNextFragment(context, ClaimListFragment.TAG,
                createDisplayClaimFragment(createBundleFromClaim(claim)));
    }

    private Bundle createBundleFromClaim(Claim claim) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.Serializable.Claim, claim);
        return bundle;
    }

    private DisplayClaimFragment createDisplayClaimFragment(Bundle extras) {
        DisplayClaimFragment displayClaimFragment = new DisplayClaimFragment();
        displayClaimFragment.setArguments(extras);
        return displayClaimFragment;
    }
}
