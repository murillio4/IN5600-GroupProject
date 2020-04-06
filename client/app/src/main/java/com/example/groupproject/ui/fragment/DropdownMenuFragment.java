package com.example.groupproject.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.groupproject.R;
import com.example.groupproject.data.model.Person;
import com.example.groupproject.ui.viewModel.SessionViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatDialogFragment;

public class DropdownMenuFragment extends DaggerAppCompatDialogFragment implements View.OnClickListener {
    private static final String TAG = "DropdownMenuFragment";

    @Inject
    SessionViewModel sessionViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_Design_Light_TopSheetDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setGravity(Gravity.TOP| Gravity.FILL_HORIZONTAL);
        View view =  inflater.inflate(R.layout.fragment_dropdown_menu, container, false);

        Person signedInUser = sessionViewModel.getSession();
        if (signedInUser != null) {
            TextView name = view.findViewById(R.id.signed_in_user_name);
            name.setText(signedInUser.getName());
        }

        ImageView close = view.findViewById(R.id.close_dropdown_menu);
        close.setOnClickListener(this);

        Button signOut = view.findViewById(R.id.sign_out);
        signOut.setOnClickListener(this);

        return view;
    }

    public void showNow(FragmentManager fragmentManager) {
        super.showNow(fragmentManager, TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_dropdown_menu:
                super.dismiss();
                break;
            case R.id.sign_out:
                sessionViewModel.removeSession();
                super.dismiss();
                break;
            default:
                break;
        }
    }
}
