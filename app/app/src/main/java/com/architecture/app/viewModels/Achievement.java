package com.architecture.app.viewModels;

public class Achievement {
    public final String label;
    public final String description;
    public final String preview;
    public final String condition;

    public Achievement(String label, String description, String preview, String condition) {
        this.label = label;
        this.description = description;
        this.preview = preview;
        this.condition = condition;
    }
}
