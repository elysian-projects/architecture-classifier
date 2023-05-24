package com.architecture.app.viewModels;

import android.content.Context;
import android.view.View;

import com.architecture.app.components.AchievementSnackBar;
import com.architecture.app.constants.Assets;
import com.architecture.app.utils.AssetsParser;

import java.io.IOException;

public class AcquiredAchievementNode {
    public final String condition;
    public boolean acquired;

    public AcquiredAchievementNode(String condition, boolean acquired) {
        this.condition = condition;
        this.acquired = acquired;
    }

    public void setAcquired() {
        this.acquired = true;
    }

    public static void triggerNewAchievement(Context context, View rootView, AcquiredAchievementNode[] acquiredAchievementNodes, String condition) throws IOException {
        for(AcquiredAchievementNode achievementNode : acquiredAchievementNodes) {
            if(achievementNode.condition.equalsIgnoreCase(condition)) {
                achievementNode.setAcquired();
                break;
            }
        }

        AssetsParser.writeNodes(context, acquiredAchievementNodes, Assets.ACHIEVEMENTS_DATA);

        Achievement[] achievements = AssetsParser.parseAchievements(context);
        Achievement currentAchievement = null;

        for(Achievement achievement : achievements) {
            if(achievement.condition.equalsIgnoreCase(condition)) {
                currentAchievement = achievement;
                break;
            }
        }

        if(currentAchievement != null) {
            AchievementSnackBar.openSnackBar(currentAchievement, context, rootView);
        }
    }
}
