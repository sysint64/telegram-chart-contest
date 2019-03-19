package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Paint;

final class AxisVertex {
    long x;
    long y;
    final String title;
    Paint.Align textAlign = Paint.Align.CENTER;
    float stateOpacity = 0f;
    float opacity = 0f;
    boolean isInit = false;
    final AxisVertex original;

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
}
