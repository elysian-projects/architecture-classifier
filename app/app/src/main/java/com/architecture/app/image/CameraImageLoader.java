package com.architecture.app.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.core.content.FileProvider;

import com.architecture.app.BuildConfig;

import java.io.File;
import java.io.IOException;

public class CameraImageLoader extends AbstractImageLoader {
    private static final String REGISTRY_KEY = "CameraImageLoader";

    private ActivityResultLauncher<Uri> _takePhotoLauncher;
    private Uri _tempImageUri;

    public CameraImageLoader(ActivityResultRegistry activityResultRegistry, Context context) {
        super(activityResultRegistry, context);

        registerAction();
    }

    @Override
    public Bitmap runLoader() {
        _tempImageUri = getTempFileUri();
        _takePhotoLauncher.launch(_tempImageUri);

        try {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContext().getContentResolver(), _tempImageUri));
        } catch(IOException exception) {
            return null;
        }
    }

    private Uri getTempFileUri() {
        try {
            File tempFile = File.createTempFile("temp_image_file", ".png");

            tempFile.createNewFile();
            tempFile.deleteOnExit();

            return FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", tempFile);
        } catch (IOException e) {
            Log.i("Upload", e.getMessage());
            throw new IllegalStateException("Errororroror");
        }
    }

    private void registerAction() {
//        _takePhotoLauncher = getRegistry().register(REGISTRY_KEY, new ActivityResultContracts.GetContent(), success -> {
////            if(success != null) {
////                _tempImageUri = success;
////            }
//        });
    }
}
