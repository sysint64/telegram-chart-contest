package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class ChartData implements Comparable<ChartData> {
    public final String name;
    public final int color;
    public float weight = 0;
    final List<Vertex> originalData = new ArrayList<>();
    final List<Vertex> minimapInnerPreviewPool = new ArrayList<>();
    final List<Vertex> minimapPointsPool = new ArrayList<>();
    final List<Vertex> previewPointsPool = new ArrayList<>();

    final List<Vertex> minimapPoints = new ArrayList<>();
    final List<Vertex> previewPoints = new ArrayList<>();

    float stateOpacity = 1f;
    float opacity = 1f;
    public boolean isVisible = true;
    float stateMinimapMaxY = 0f;
    float minimapMaxY = 0f;
    boolean isMinimapMaxYInit = false;

    ChartData(String name, String color) {
        this.name = name;
        this.color = Color.parseColor(color);
    }

    enum SourceType {
        MINIMAP,
        PREVIEW
    }

    @Override
    public int compareTo(ChartData o) {
        return (int) (o.weight - this.weight);
    }
}
