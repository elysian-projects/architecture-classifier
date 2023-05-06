package com.architecture.app.model;

import com.architecture.app.viewModels.ArchitectureNode;

public class ModelResponse {
    public static final String SUCCESSFUL_RESPONSE_SHORT = "С большой вероятностью, на фотографии изображён этот тип архитектуры";
    public static final String FAILED_RESPONSE_SHORT = "К сожалению, нам не удалось определить этот вид архитектуры";

    private final ArchitectureNode _node;
    private final boolean _ok;

    public ModelResponse(ArchitectureNode node, boolean ok) {
        _node = node;
        _ok = ok;
    }

    public ArchitectureNode node() {
        return _node;
    }

    public boolean ok() {
        return _ok;
    }
}
