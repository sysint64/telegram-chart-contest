package ru.kabylin.andrey.telegramcontest.helpers;

public class MathUtils {
    static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }

        if (value > max) {
            return max;
        }

        return value;
    }
}
