package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Paint;

final class AxisVertex {
    long x;
    long y;
    String title;
    Paint.Align textAlign = Paint.Align.CENTER;
    float stateOpacity = 1f;
    float opacity = 1f;

    float stateYOffset = 0f;
    float yOffset = 0f;

    AxisVertex original;

    AxisVertex(long x, long y, String title) {
        this.x = x;
        this.y = y;
        this.title = title;
        this.original = this;
    }

    AxisVertex(long x, long y, String title, AxisVertex original) {
        this.x = x;
        this.y = y;
        this.title = title;
        this.original = original;
    }

    AxisVertex(long x, long y, String title, float stateOpacity, float opacity, AxisVertex original) {
        this.x = x;
        this.y = y;
        this.title = title;
        this.stateOpacity = stateOpacity;
        this.opacity = opacity;
        this.original = original;
    }

    AxisVertex(long x, long y, String title, float stateOpacity, float opacity, AxisVertex original, float yOffset, float stateYOffset) {
        this.x = x;
        this.y = y;
        this.title = title;
        this.stateOpacity = stateOpacity;
        this.opacity = opacity;
        this.original = original;
        this.yOffset = yOffset;
        this.stateYOffset = stateYOffset;
    }
}
