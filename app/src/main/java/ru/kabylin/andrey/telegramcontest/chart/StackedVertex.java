package ru.kabylin.andrey.telegramcontest.chart;

final class StackedVertex implements Comparable<StackedVertex> {
    final float y;
    final float opacity;
    final int color;

    StackedVertex(float y, float opacity, int color) {
        this.y = y;
        this.opacity = opacity;
        this.color = color;
    }

    @Override
    public int compareTo(StackedVertex o) {
        return (int) (o.y - this.y);
    }
}

