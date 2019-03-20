package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Rect;

public interface ChartSolver {
    ChartState getState();

    void calculateMinimapPoints(final Rect rect);

    void calculatePreviewPoints(final Rect rect);

    void setMinimapPosition(final float newPosition);

    void setMinimapLeft(final float newLeft);

    void setMinimapRight(final float newRight);

    void onProgress();

    boolean setChartVisibilityByName(final String name, final boolean visibility);

    void calculateAxisXPoints(final Rect rect);

    void calculateAxisYPoints(final Rect rect);

    void setChartState(ChartState chartState);
}
