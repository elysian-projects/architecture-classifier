package com.architecture.app.permission;

import android.Manifest;

public class Permissions {
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    public static final int CAMERA_REQUEST_CODE = 11;
    public static final int STORAGE_REQUEST_CODE = 12;
}
