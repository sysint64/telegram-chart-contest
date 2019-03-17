package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Rect;
import ru.kabylin.andrey.telegramcontest.helpers.MathUtils;

import java.util.ArrayList;
import java.util.List;

// TODO: Encapsulation
class ChartState {
    int minimapPreviewLeft = 100;
    int minimapPreviewRight = minimapPreviewLeft + 100;
    int minimapPreviewResizeAreaSize = 20;
    Rect minimapRect = null;
    boolean isInitPreviewMaxY = false;
    float statePreviewMaxY = 0f;
    float previewMaxY = 0f;
    float previewMaxYChangeSpeed = 100f;

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
