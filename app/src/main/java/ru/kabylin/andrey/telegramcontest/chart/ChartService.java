package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Rect;

public interface ChartService {
    ChartState getState();

    void calculateMinimapPoints(final Rect rect);

    void calculatePreviewPoints(final Rect rect);

    void setMinimapPoisiton(final float newPosition);
}
