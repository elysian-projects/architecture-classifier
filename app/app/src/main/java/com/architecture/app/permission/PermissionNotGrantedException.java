package com.architecture.app.permission;

public class PermissionNotGrantedException extends Exception {
    public PermissionNotGrantedException(String message) {
        super(message);
    }

    public PermissionNotGrantedException() {
        super("Permission not granted!");
    }
}
