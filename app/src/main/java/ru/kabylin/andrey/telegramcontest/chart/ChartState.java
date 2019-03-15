package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

// TODO: Encapsulation
class ChartState {
    int minimapPreviewLeft = 100;
    int minimapPreviewRight = minimapPreviewLeft + 100;
    int minimapPreviewResizeAreaSize = 40;
    Rect minimapRect = null;

    List<ChartData> charts = new ArrayList<>();

    Rect getMinimapPreviewRect() {
        return new Rect(
                minimapPreviewLeft,
                minimapRect.top,
                minimapPreviewRight,
                minimapRect.bottom
        );
    }

    int minimapPreviewSize() {
        return minimapPreviewRight - minimapPreviewLeft;
    }
}
