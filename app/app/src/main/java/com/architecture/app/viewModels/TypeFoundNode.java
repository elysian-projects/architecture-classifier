package com.architecture.app.viewModels;

public class TypeFoundNode {
    public static final int DEFAULT_FOUND_TIMES = 0;

    public final String value;
    public final int foundTimes;

    public TypeFoundNode(String value, int foundTimes) {
        this.value = value;
        this.foundTimes = foundTimes;
    }
}
