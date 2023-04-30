package com.architecture.app.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.InputStream;
import java.util.Scanner;

public class JSONFileParser implements FileParser {
    @Override
    public <T> T parse(InputStream filePath, Class<T> type) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(readFileData(filePath), type);
    }

    private String readFileData(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        String fileData = "";

        while(scanner.hasNext()) {
            fileData += scanner.nextLine() + "\n";
        }

        return fileData;
    }
}
