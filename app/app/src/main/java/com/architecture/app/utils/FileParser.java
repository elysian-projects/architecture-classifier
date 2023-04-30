package com.architecture.app.utils;

import java.io.IOException;
import java.io.InputStream;

public interface FileParser {
    <T> T parse(InputStream filePath, Class<T> type) throws IOException;
}
