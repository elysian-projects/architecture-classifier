package com.architecture.app.utils;

public class Localization {
    public static String chooseProperEnding(int amount, String one, String two, String five) {
        int last = amount % 10;

        if(last == 1) {
            return one;
        }

        int lastTwo = amount % 100;

        if(lastTwo > 1 && lastTwo < 5) {
            return two;
        }

        return five;
    }
}
