package com.example.groupproject.data.util;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.groupproject.R;
import com.example.groupproject.ui.abstraction.TaggedDialogFragment;
import com.example.groupproject.ui.fragment.LocationPickerDialogFragment;

public class TransitionUtil {

    private static final String TAG = "TransitionUtil";
    
    public static void toNextFragment(@Nullable FragmentActivity activity, String tag, Fragment next) {
        if (activity != null) {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, next)
                    .addToBackStack(tag)
                    .commit();
        } else {
            Log.e(TAG, "toNextFragment: Could not move to next fragment. Activity is null");
        }
    }

    public static void toPreviousFragment(@Nullable FragmentActivity activity) {
        if (activity != null) {
            activity.getSupportFragmentManager().popBackStack();
        } else {
            Log.e(TAG, "toNextFragment: Could not move to previous fragment. Activity is null");
        }
    }

    public static void openDialog(@Nullable FragmentActivity activity, TaggedDialogFragment dialog) {
        if (activity != null) {
            FragmentManager fm = activity.getSupportFragmentManager();
            dialog.showNow(fm);
        } else {
            Log.e(TAG, "openDialog: Could not open dialog fragment. Activity is null");
        }
    }
}
