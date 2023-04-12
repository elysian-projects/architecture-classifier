package com.architecture.app.model;

public class ModelResponse {
    public static String SUCCESSFUL_RESPONSE_SHORT = "С большой вероятностью, на фотографии изображён этот тип архитектуры";
    public static String FAILED_RESPONSE_SHORT = "К сожалению, нам не удалось определить этот вид архитектуры";

    private final String _message;
    private final boolean _found;

    public ModelResponse(String message, boolean found) {
        _message = message;
        _found = found;
    }

    public String message() {
        return _message;
    }

    public boolean found() {
        return _found;
    }
}
