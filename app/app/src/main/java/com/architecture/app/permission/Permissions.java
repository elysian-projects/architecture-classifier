package com.architecture.app.permission;

import android.content.Intent;
import android.provider.MediaStore;

public class Permissions {
    public static final String CAMERA = MediaStore.ACTION_IMAGE_CAPTURE;
    public static final String READ_STORAGE = Intent.ACTION_GET_CONTENT;

    public static final int CAMERA_REQUEST_CODE = 11;
    public static final int STORAGE_REQUEST_CODE = 12;
}
