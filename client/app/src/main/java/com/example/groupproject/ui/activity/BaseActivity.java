package com.example.groupproject.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.groupproject.R;
import com.example.groupproject.ui.fragment.DropdownMenuFragment;

public abstract class BaseActivity extends SessionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_menu:
                openDropdownMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openDropdownMenu() {
        FragmentManager fm = getSupportFragmentManager();
        new DropdownMenuFragment().showNow(fm);
    }
}
