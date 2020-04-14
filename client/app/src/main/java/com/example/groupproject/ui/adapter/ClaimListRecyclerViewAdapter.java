package com.example.groupproject.ui.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groupproject.R;
import com.example.groupproject.data.Constants;
import com.example.groupproject.data.model.Claim;
import com.example.groupproject.data.model.ClaimList;
import com.example.groupproject.ui.fragment.DisplayClaimFragment;

public class ClaimListRecyclerViewAdapter extends RecyclerView.Adapter<ClaimListItemViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

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
                .setListItemText(claim.getId())
                .setParentLayoutOnClickListner(v -> startDisplayClaimFragment(claim));
    }

    @Override
    public int getItemCount() {
        return claimList.getClaims().size();
    }

    private void startDisplayClaimFragment(Claim claim) {
        context.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container,
                        createDisplayClaimFragment(createBundleFromClaim(claim)))
                .addToBackStack(null)
                .commit();
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
