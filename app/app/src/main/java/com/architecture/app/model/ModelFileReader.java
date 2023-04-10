package com.architecture.app.model;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ModelFileReader {
    private static final String CLASSNAMES_PATH = "classNames.txt";

    private final Context _context;

    public ModelFileReader(Context context) {
        _context = context;
    }

    public List<String> readClassNamesList() throws IOException {
        try(InputStream inputStream = _context.getAssets().open(CLASSNAMES_PATH)) {
            Scanner scanner = new Scanner(inputStream);
            List<String> tempList = new ArrayList<>();

            while(scanner.hasNext()) {
                tempList.add(scanner.nextLine());
            }

            return tempList;
        }
    }
}
