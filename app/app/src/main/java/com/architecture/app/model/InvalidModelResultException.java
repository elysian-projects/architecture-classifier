package com.architecture.app.model;

public class InvalidModelResultException extends Exception {
    public InvalidModelResultException() {
        super("Invalid model result exception!");
    }

    public InvalidModelResultException(String message) {
        super(message);
    }
}
