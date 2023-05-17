package com.architecture.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.architecture.app.constants.Assets;
import com.architecture.app.viewModels.TypeFoundNode;
import com.architecture.app.viewModels.ArchitectureNode;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AssetsParser {
    private static final List<ArchitectureNode> _cachedArchitectureTypes = new ArrayList<>();

    public static Bitmap readPreviewImage(Context context, String image) throws IOException {
        return BitmapFactory.decodeStream(context.getAssets().open("previews/" + image));
    }

    public static void writeFoundNodes(Context context, TypeFoundNode[] foundNodes) throws IOException {
        File storageData = new File(context.getExternalFilesDir("").getAbsolutePath(), Assets.TYPES_FOUND_DATA);

        String data = new Gson().toJson(foundNodes);

        try(FileWriter fileWriter = new FileWriter(storageData)) {
            fileWriter.append(data);
            fileWriter.flush();
        }

        Log.i("AssetsParser", "Successfully wrote found nodes:");
        Log.i("AssetsParser", data);
    }

    public static ArchitectureNode[] parseArchitectureTypes(Context context) throws IOException {
        if(_cachedArchitectureTypes.size() != 0) {
            return _cachedArchitectureTypes.toArray(new ArchitectureNode[] {});
        }

        ArchitectureNode[] nodes = new JSONFileParser().parse(
            context.getAssets().open(Assets.ARCHITECTURE_TYPES),
            ArchitectureNode[].class
        );

        _cachedArchitectureTypes.addAll(Arrays.asList(nodes));

        return nodes;
    }

    public static TypeFoundNode[] parseTypesFoundData(Context context) throws IOException {
        File storageData = new File(context.getExternalFilesDir("").getAbsolutePath(), Assets.TYPES_FOUND_DATA);

        if(!storageData.exists()) {
            return writeDefaultDataToFileAndGetData(context);
        }

        try(FileReader fileReader = new FileReader(storageData)) {
            Scanner scanner = new Scanner(fileReader);
            String result = "";

            while(scanner.hasNext()) {
                result += scanner.nextLine();
            }

            return new JSONFileParser().parse(result, TypeFoundNode[].class);
        }
    }

    private static TypeFoundNode[] writeDefaultDataToFileAndGetData(Context context) throws IOException {
        ArchitectureNode[] architectureNodes = new JSONFileParser().parse(
            context.getAssets().open(Assets.ARCHITECTURE_TYPES),
            ArchitectureNode[].class
        );

        TypeFoundNode[] foundNodesAsArray = getArrayWithDefaultObjects(architectureNodes);

        writeFoundNodes(context, foundNodesAsArray);

        return foundNodesAsArray;
    }

    private static TypeFoundNode[] getArrayWithDefaultObjects(ArchitectureNode[] architectureNodes) {
        List<TypeFoundNode> foundNodes = new ArrayList<>();
        String[] classNameValues = Arrays.stream(architectureNodes).map(node -> node.value).toArray(String[]::new);

        Arrays.asList(classNameValues).forEach(value -> foundNodes.add(new TypeFoundNode(value, TypeFoundNode.DEFAULT_FOUND_TIMES)));

        return foundNodes.toArray(new TypeFoundNode[] {});
    }
}
