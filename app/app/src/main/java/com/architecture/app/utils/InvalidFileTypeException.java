package com.architecture.app.utils;

public class InvalidFileTypeException extends Exception {
    public InvalidFileTypeException(String message) {
        super(message);
    }

    public InvalidFileTypeException() {
        super("Invalid file type given!");
    }
}
