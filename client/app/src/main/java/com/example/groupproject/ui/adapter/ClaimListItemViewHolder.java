package com.example.groupproject.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.groupproject.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClaimListItemViewHolder extends RecyclerView.ViewHolder
        implements RequestListener<Bitmap> {

    private static final String TAG = "ClaimListItemViewHolder";

    private CircleImageView claimListItemImage;
    private TextView claimListItemText;
    private RelativeLayout parentLayout;

    public ClaimListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.claimListItemImage = itemView.findViewById(R.id.claim_list_item_image);
        this.claimListItemText = itemView.findViewById(R.id.claim_list_item_text);
        this.parentLayout = itemView.findViewById(R.id.claim_list_item);
    }

    public ClaimListItemViewHolder setListItemImage(@NonNull  Context context, String imagePath) {
        Glide.with(context)
                .asBitmap()
                .addListener(this)
                .load(imagePath)
                .error(R.drawable.ic_error_red_24dp)
                .into(this.claimListItemImage);

        return this;
    }

    public ClaimListItemViewHolder setListItemText(String text) {
        this.claimListItemText.setText(text);
        return this;
    }

    public ClaimListItemViewHolder setParentLayoutOnClickListner(
            @Nullable View.OnClickListener onClickListener) {
        this.parentLayout.setOnClickListener(onClickListener);
        return this;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target,
                                boolean isFirstResource) {
        Log.d(TAG, "onLoadFailed: Failed to load image:");
        return false;
    }

    @Override
    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target,
                                   DataSource dataSource, boolean isFirstResource) {
        return false;
    }
}
