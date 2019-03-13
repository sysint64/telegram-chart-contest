package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

// TODO: Encapsulation
class ChartState {
    int minimapPreviewPosition = 100;
    int minimapPreviewSize = 100;
    Rect minimapRect = null;

    List<ChartData> charts = new ArrayList<>();

    Rect getMinimapPreviewRect() {
        return new Rect(
                minimapPreviewPosition,
                minimapRect.top,
                minimapPreviewPosition + minimapPreviewSize,
                minimapRect.bottom
        );
    }
}
