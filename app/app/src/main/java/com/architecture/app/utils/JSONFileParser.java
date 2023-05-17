package com.architecture.app.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.InputStream;
import java.util.Scanner;

public class JSONFileParser implements FileParser {
    @Override
    public <T> T parse(InputStream filePath, Class<T> type) throws JsonSyntaxException {
        return parse(readFileData(filePath), type);
    }

    @Override
    public <T> T parse(String fileData, Class<T> type) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(fileData, type);
    }

    private String readFileData(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        String fileData = "";

        while(scanner.hasNext()) {
            fileData += scanner.nextLine() + "\n";
        }

        scanner.close();

        return fileData;
    }
}
