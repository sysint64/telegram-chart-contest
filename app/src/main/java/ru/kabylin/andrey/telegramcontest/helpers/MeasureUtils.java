package ru.kabylin.andrey.telegramcontest.helpers;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class MeasureUtils {
    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
