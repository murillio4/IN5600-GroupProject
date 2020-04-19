package com.example.groupproject.data.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.groupproject.R;


public class TransitionUtil {
    public static void toNextFragment(FragmentActivity activity, String tag, Fragment next) {
        activity.getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.main_fragment_container, next)
            .addToBackStack(tag)
            .commit();
    }

    public static void toPreviousFragment(FragmentActivity activity) {
        activity.getSupportFragmentManager().popBackStack();
    }
}
