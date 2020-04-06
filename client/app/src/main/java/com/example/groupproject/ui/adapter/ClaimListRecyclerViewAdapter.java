package com.example.groupproject.ui.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ClaimListRecyclerViewAdapter extends RecyclerView.Adapter<ClaimListRecyclerViewAdapter.ClaimListItemViewHolder>{

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
        View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.layout_claim_list_item, parent, false);
        ClaimListItemViewHolder claimListItemViewHolder = new ClaimListItemViewHolder(view);
        return claimListItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimListItemViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");

        Claim claim = claimList.getClaims().get(position);

        Glide.with(context)
                .asBitmap()
                .load(claim.getPhotoPath())
                .error(R.drawable.ic_error_red_24dp)
                .into(holder.claimListItemImage);

        holder.claimListItemText.setText(claim.getId());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Clicked on: " + claim.getId());
                Toast.makeText(context, "Clicked on: " + claim.getId(), Toast.LENGTH_LONG).show();

                Bundle extras = new Bundle();
                extras.putSerializable(Constants.Serializable.Claim, claim);

                DisplayClaimFragment displayClaimFragment = new DisplayClaimFragment();
                displayClaimFragment.setArguments(extras);

                FragmentTransaction fragmentTransaction =
                        context.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment_container, displayClaimFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return claimList.getClaims().size();
    }

    public class ClaimListItemViewHolder extends RecyclerView.ViewHolder {
        CircleImageView claimListItemImage;
        TextView claimListItemText;
        RelativeLayout parentLayout;

        public ClaimListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.claimListItemImage = itemView.findViewById(R.id.claim_list_item_image);
            this.claimListItemText = itemView.findViewById(R.id.claim_list_item_text);
            this.parentLayout = itemView.findViewById(R.id.claim_list_item);
        }
    }
}
