package com.architecture.app.permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;

public class Permissions {
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String READ_STORAGE = Manifest.permission.READ_MEDIA_IMAGES;

    private static final String CAMERA_LOAD_PERMISSION_KEY = "CameraPermission";

    private ActivityResultLauncher<String> _grantPermissionsLauncher;
    private PermissionGrantedCallback _permissionGrantedCallback;

    public static boolean permissionGranted(Context context, String permission) {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void grantPermission(String permission, Context context, ActivityResultRegistry registry, PermissionGrantedCallback callback) {
        if(permissionGranted(context, permission)) {
            callback.run(true);
            return;
        }

        _permissionGrantedCallback = callback;

        registerListener(registry);
        _grantPermissionsLauncher.launch(permission);
    }

    private void registerListener(ActivityResultRegistry registry) {
        _grantPermissionsLauncher = registry.register(CAMERA_LOAD_PERMISSION_KEY, new ActivityResultContracts.RequestPermission(), isGranted -> {
            if(!isGranted) {
                Log.i("Permissions", "Permission not granted");
            }

            _permissionGrantedCallback.run(isGranted);
        });
    }
}
