package com.architecture.app.utils;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;

public interface FileParser {
    <T> T parse(InputStream filePath, Class<T> type) throws IOException;
    <T> T parse(String fileData, Class<T> type) throws JsonSyntaxException;
}
