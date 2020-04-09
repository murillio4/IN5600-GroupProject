package com.example.groupproject.ui.fragment;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.groupproject.BuildConfig;
import com.example.groupproject.R;
import com.example.groupproject.ui.viewModel.PhotoViewModel;
import com.example.groupproject.ui.viewModel.StorageViewModel;
import com.karumi.dexter.DexterBuilder.Permission;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static android.app.Activity.RESULT_OK;

public class CreateClaimFragment extends DaggerFragment {

    private static final String TAG = "CreateClaimFragment";
    private static final String TMP_FILE_NAME = "80085.jpg";

    private enum REQUEST_CODE {
        CAMERA,
        GALLERY
    }

    @Inject
    StorageViewModel storageViewModel;

    @Inject
    PhotoViewModel photoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_claim, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDescriptionInput();
        initAddMapLocationButton();
        initAddPhotoButton();
        initSubmitButton();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE.CAMERA.ordinal()) {
                handleImageCaptureActivityResult(data);
            } else if (requestCode == REQUEST_CODE.GALLERY.ordinal()) {
                handleGalleryActivityResult(data);
            } else {
                // Handle ???
            }
        } else {
            // Handle ???
        }
    }

    protected void toClaimListFragment() {
        FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, new ClaimListFragment());
        fragmentTransaction.commit();
    }

    protected void showClaimSuccess() {
        ///
    }

    protected void showClaimFailed() {
        ///
    }

    protected void initDescriptionInput() {
        final EditText editText = getView().findViewById(R.id.create_claim_description_input);
    }

    protected void initAddMapLocationButton() {
        getView().findViewById(R.id.create_claim_add_map_location_button)
                .setOnClickListener(v -> {
                    Toast.makeText(getActivity(), "Add Map Location", Toast.LENGTH_SHORT).show();
                    // Do some magic here ...
                });
    }

    protected void initAddPhotoButton() {
        getView().findViewById(R.id.create_claim_add_photo_button)
                .setOnClickListener(v -> {
                    Toast.makeText(getActivity(), "Add Photo", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    new PhotoDialogFragment().showNow(fm);
                });
    }

    protected void initSubmitButton() {
        getView().findViewById(R.id.create_claim_submit_button)
                .setOnClickListener(v -> {
                    Toast.makeText(getActivity(), "Submit", Toast.LENGTH_SHORT).show();
                    // Do some magic here ...
                    toClaimListFragment();
                });
    }

    private void addImage() {
        final String[] options = { "Take Photo", "Choose from Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select an Option");
        builder.setItems(options, (dialog, which) -> {
            switch (options[which]) {
                case "Take Photo":
                    startImageCaptureActivity();
                    break;
                case "Choose from Gallery":
                    startGalleryActivity();
                    break;
                case "Cancel":
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        });
        builder.show();
    }

    private void startImageCaptureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Context context = getContext();

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            try {
                File imageFile = storageViewModel.createImageFile(
                        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));

                if (imageFile != null) {
                    Uri imageUri = FileProvider.getUriForFile(
                            context, BuildConfig.APPLICATION_ID + ".provider", imageFile);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, REQUEST_CODE.CAMERA.ordinal());
                }
            } catch (IOException e) {
                Log.e(TAG, "startImageCaptureActivity: Error creating image file", e);
            }
        }
    }

    private void startGalleryActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE.GALLERY.ordinal());
        }
    }

    private void handleImageCaptureActivityResult(Intent data) {
    }

    private void handleGalleryActivityResult(Intent data) {
        Uri selectedImage = data.getData();
        Toast.makeText(getActivity(), "Image: " + selectedImage, Toast.LENGTH_SHORT).show();
    }
}
