package ru.kabylin.andrey.telegramcontest.helpers;

public class MathUtils {
    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }

        if (value > max) {
            return max;
        }

        return value;
    }

    public static float interpTo(float current, float target, float deltaTime, float interpSpeed) {
        if (interpSpeed == 0) {
            return target;
        }

        float dist = target - current;

        if (dist * dist < 0.1f) {
            return target;
        }

        float deltaMove = dist * deltaTime * interpSpeed;
        return current + deltaMove;
    }
}
