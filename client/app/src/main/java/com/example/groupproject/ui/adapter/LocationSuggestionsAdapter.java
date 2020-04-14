package com.example.groupproject.ui.adapter;

import android.bluetooth.le.ScanCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject.R;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

public class LocationSuggestionsAdapter extends SuggestionsAdapter<String, LocationSuggestionsAdapter.SuggestionHolder> {
    private SuggestionsAdapter.OnItemViewClickListener listener;

    public LocationSuggestionsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public void setListener(SuggestionsAdapter.OnItemViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getSingleViewHeight() {
        return 50;
    }

    @NonNull
    @Override
    public LocationSuggestionsAdapter.SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.location_suggestion, parent, false);
        return new LocationSuggestionsAdapter.SuggestionHolder(view);
    }

    @Override
    public void onBindSuggestionHolder(String suggestion, LocationSuggestionsAdapter.SuggestionHolder holder, int position) {
        holder.text.setText(getSuggestions().get(position));
    }

    public interface OnItemViewClickListener {
        void OnItemClickListener(int position, View v);

        void OnItemDeleteListener(int position, View v);
    }

    class SuggestionHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        private final ImageView delete;

        SuggestionHolder(final View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.location_suggestion_text);
            delete = itemView.findViewById(R.id.location_suggestion_delete_btn);

            itemView.setOnClickListener(v -> {
                v.setTag(getSuggestions().get(getAdapterPosition()));
                listener.OnItemClickListener(getAdapterPosition(), v);
            });

            delete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position > 0 && position < getSuggestions().size()) {
                    v.setTag(getSuggestions().get(getAdapterPosition()));
                    listener.OnItemDeleteListener(getAdapterPosition(), v);
                }
            });
        }
    }
}