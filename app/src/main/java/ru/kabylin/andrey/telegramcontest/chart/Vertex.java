package ru.kabylin.andrey.telegramcontest.chart;

class Vertex {
    final long x;
    final long y;

    Vertex(long x, long y) {
        this.x = x;
        this.y = y;
    }

    Vertex(float x, float y) {
        this.x = (long) x;
        this.y = (long) y;
    }
}
