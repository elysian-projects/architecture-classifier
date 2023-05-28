package com.architecture.app.utils;

import android.content.Context;

import com.architecture.app.constants.Assets;
import com.architecture.app.viewModels.AcquiredAchievementNode;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AssetsAchievements {
    public static AcquiredAchievementNode[] parseAcquiredAchievementsData(Context context) throws IOException {
        File storageData = new File(context.getExternalFilesDir("").getAbsolutePath(), Assets.ACHIEVEMENTS_DATA);

        if(!storageData.exists()) {
            return writeDefaultAchievementsDataToFileAndGetData(context);
        }

        try(FileReader fileReader = new FileReader(storageData)) {
            Scanner scanner = new Scanner(fileReader);
            String result = "";

            while(scanner.hasNext()) {
                result += scanner.nextLine();
            }

            return new JSONFileParser().parse(result, AcquiredAchievementNode[].class);
        }
    }

    private static AcquiredAchievementNode[] writeDefaultAchievementsDataToFileAndGetData(Context context) throws IOException {
        AcquiredAchievementNode[] architectureNodes = new JSONFileParser().parse(
                context.getAssets().open(Assets.ACHIEVEMENTS),
                AcquiredAchievementNode[].class
        );

        AcquiredAchievementNode[] foundNodesAsArray = getAchievementsDataArrayWithDefaultObjects(architectureNodes);
        AssetsParser.writeNodes(context, foundNodesAsArray, Assets.ACHIEVEMENTS_DATA);

        return foundNodesAsArray;
    }

    private static AcquiredAchievementNode[] getAchievementsDataArrayWithDefaultObjects(AcquiredAchievementNode[] achievementsDataNodes) {
        List<AcquiredAchievementNode> achievementsNodes = new ArrayList<>();
        String[] conditionValues = Arrays.stream(achievementsDataNodes).map(node -> node.condition).toArray(String[]::new);

        Arrays.asList(conditionValues).forEach(value -> achievementsNodes.add(new AcquiredAchievementNode(value, false)));

        return achievementsNodes.toArray(new AcquiredAchievementNode[] {});
    }
}
