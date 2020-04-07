package com.example.groupproject.ui.fragment;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import com.example.groupproject.BuildConfig;
import com.example.groupproject.R;
import com.example.groupproject.ui.viewModel.StorageViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

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
                    addImage();
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
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Take Photo")) {
                    startImageCaptureActivity();
                } else if (options[which].equals("Choose from Gallery")) {
                    startGalleryActivity();
                } else if (options[which].equals("Cancel")) {
                    dialog.dismiss();
                } else {
                    // Handle this?
                }
            }
        });
        builder.show();
    }

    private void startImageCaptureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Context context = getContext();

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            File imageFile = null;

            try {
                imageFile = storageViewModel.createImageFile(
                        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            } catch (IOException e) {
                e.printStackTrace();
                // Handle better?
            }

            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(
                        context, BuildConfig.APPLICATION_ID + ".provider", imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFile);
                startActivityForResult(intent, REQUEST_CODE.CAMERA.ordinal());
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
