package ru.kabylin.andrey.telegramcontest.chart;

final class StackedVertex implements Comparable<StackedVertex> {
    float y;
    final float opacity;
    final int color;
    final float scale;

    StackedVertex(float y, float opacity, int color, float scale) {
        this.y = y;
        this.opacity = opacity;
        this.color = color;
        this.scale = scale;
    }

    @Override
    public int compareTo(StackedVertex o) {
        return (int) (o.y - this.y);
    }
}

