package com.architecture.app.utils;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class JSONFileParser implements FileParser {
    public static final String JSON_EXTENSION = "json";

    @Override
    public <T> T parse(String filePath, Class<T> type) throws InvalidFileTypeException, IOException {
        if(!isJsonFile(filePath)) {
            throw new InvalidFileTypeException();
        }

        Gson gson = new Gson();
        return gson.fromJson(readFileData(filePath), type);
    }

    @Override
    public <T> T parse(InputStream stream, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(readFileData(stream), type);
    }

    private String readFileData(String filePath) throws IOException {
        try(FileReader fileReader = new FileReader(filePath)) {
            return readFileData(fileReader);
        }
    }

    // FIXME: wtf is this?
    private String readFileData(Readable stream) {
        Scanner scanner = new Scanner(stream);

        String fileData = "";

        while(scanner.hasNext()) {
            fileData += scanner.nextLine() + "\n";
        }

        return fileData;
    }

    private String readFileData(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        String fileData = "";

        while(scanner.hasNext()) {
            fileData += scanner.nextLine() + "\n";
        }

        return fileData;
    }

    private boolean isJsonFile(String filePath) {
        try {
            String[] filePathSplitWithDot = filePath.split("\\.");
            String extension = filePathSplitWithDot[filePathSplitWithDot.length - 1];

            return fileExists(filePath) && extension.equals(JSON_EXTENSION);
        } catch(Exception ignored) {
            return false;
        }
    }

    private boolean fileExists(String filePath) {
        try {
            return new File(filePath).exists();
        } catch(Exception ignored) {
            return false;
        }
    }
}
