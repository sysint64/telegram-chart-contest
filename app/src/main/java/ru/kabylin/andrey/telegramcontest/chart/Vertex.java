package ru.kabylin.andrey.telegramcontest.chart;

class Vertex {
    float x = 0;
    float y = 0;
    float yCopy = 0;
    String title = "";
    String yValue = "";
    String xValue = "";
    int color = 0;
    long originalX;

    Vertex() {
    }

    Vertex(long originalX, long x, long y, String title, String xValue, String value, int color) {
        this.originalX = originalX;
        this.x = x;
        this.y = y;
        this.title = title;
        this.xValue = xValue;
        this.yValue = value;
        this.color = color;
    }
}
