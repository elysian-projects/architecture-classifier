package com.architecture.app.permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.MediaStore;

public class Permissions {
    public static final String CAMERA = MediaStore.ACTION_IMAGE_CAPTURE;
    public static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    public static final int CAMERA_REQUEST_CODE = 11;
    public static final int STORAGE_REQUEST_CODE = 12;

    public static boolean permissionGranted(Context context, String permission) {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
