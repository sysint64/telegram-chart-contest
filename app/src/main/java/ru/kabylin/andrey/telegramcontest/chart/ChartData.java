package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

class ChartData {
    final String name;
    final int color;
    final List<Vertex> originalData = new ArrayList<>();
    final List<Vertex> minimapInnerPreviewPool = new ArrayList<>();
    final List<Vertex> minimapPointsPool = new ArrayList<>();
    final List<Vertex> previewPointsPool = new ArrayList<>();

    final List<Vertex> minimapPoints = new ArrayList<>();
    final List<Vertex> previewPoints = new ArrayList<>();

    float stateOpacity = 1f;
    float opacity = 1f;
    boolean isVisible = true;
    float stateMinimapMaxY = 0f;
    float minimapMaxY = 0f;
    boolean isMinimapMaxYInit = false;

    ChartData(String name, String color) {
        this.name = name;
        this.color = Color.parseColor(color);
    }
}
