package ru.kabylin.andrey.telegramcontest.chart;

import android.content.res.AssetManager;
import android.graphics.Rect;
import android.support.annotation.Nullable;

public interface ChartSolver {
    ChartState getState();

    void calculateMinimapPoints(final Rect rect);

    void calculate2YMinimapPoints(final Rect rect);

    void calculatePreviewPoints(final Rect rect);

    void calculate2YPreviewPoints(final Rect rect);

    void setMinimapPosition(final float newPosition);

    void setMinimapLeft(final float newLeft);

    void setMinimapRight(final float newRight);

    void onProgress(final float deltaTime);

    boolean setChartVisibilityByName(final String name, final boolean visibility);

    void calculateAxisXPoints(final Rect rect);

    void calculateAxisYPoints(final Rect rect);

    void setChartState(ChartState chartState);

    void dropPopup(final float touchX, final float touchY);

    void hidePopup();

    @Nullable
    ChartState zoomIn(AssetManager assetManager);

    void zoomIn();

    void zoomOut();

    void setOnChangeChartVisibleRangeListener(final OnChangeChartVisibleRangeListener onChangeChartVisibleRangeListener);
}
