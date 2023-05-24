package com.architecture.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.architecture.app.constants.Assets;
import com.architecture.app.viewModels.Achievement;
import com.architecture.app.viewModels.TypeFoundNode;
import com.architecture.app.viewModels.ArchitectureNode;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AssetsParser {
    private static final List<ArchitectureNode> _cachedArchitectureTypes = new ArrayList<>();
    private static final List<Achievement> _cachedAchievements = new ArrayList<>();

    public static Bitmap readPreviewImage(Context context, String image) throws IOException {
        return BitmapFactory.decodeStream(context.getAssets().open("previews/" + image));
    }

    public static <T> void writeNodes(Context context, T[] foundNodes, String path) throws IOException {
        File storageData = new File(context.getExternalFilesDir("").getAbsolutePath(), path);

        String data = new Gson().toJson(foundNodes);

        try(FileWriter fileWriter = new FileWriter(storageData)) {
            fileWriter.append(data);
            fileWriter.flush();
        }

        Log.i("AssetsParser", "Successfully wrote data:");
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

    public static Achievement[] parseAchievements(Context context) throws IOException {
        if(_cachedAchievements.size() != 0) {
            return _cachedAchievements.toArray(new Achievement[] {});
        }

        Achievement[] nodes = new JSONFileParser().parse(
            context.getAssets().open(Assets.ACHIEVEMENTS),
            Achievement[].class
        );

        _cachedAchievements.addAll(Arrays.asList(nodes));

        return nodes;
    }

    public static void resetData(Context context) throws IOException {
        Files.deleteIfExists(new File(context.getExternalFilesDir("").getAbsolutePath(), Assets.TYPES_FOUND_DATA).toPath());
        Files.deleteIfExists(new File(context.getExternalFilesDir("").getAbsolutePath(), Assets.ACHIEVEMENTS_DATA).toPath());
    }

    private static TypeFoundNode[] writeDefaultDataToFileAndGetData(Context context) throws IOException {
        ArchitectureNode[] architectureNodes = new JSONFileParser().parse(
            context.getAssets().open(Assets.ARCHITECTURE_TYPES),
            ArchitectureNode[].class
        );

        TypeFoundNode[] foundNodesAsArray = getArrayWithDefaultObjects(architectureNodes);

        writeNodes(context, foundNodesAsArray, Assets.TYPES_FOUND_DATA);

        return foundNodesAsArray;
    }

    private static TypeFoundNode[] getArrayWithDefaultObjects(ArchitectureNode[] architectureNodes) {
        List<TypeFoundNode> foundNodes = new ArrayList<>();
        String[] classNameValues = Arrays.stream(architectureNodes).map(node -> node.value).toArray(String[]::new);

        Arrays.asList(classNameValues).forEach(value -> foundNodes.add(new TypeFoundNode(value, TypeFoundNode.DEFAULT_FOUND_TIMES)));

        return foundNodes.toArray(new TypeFoundNode[] {});
    }
}
