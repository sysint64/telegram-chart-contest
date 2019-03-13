package ru.kabylin.andrey.telegramcontest.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartData {
    final String name;
    final String color;
    final List<Vertex> originalData = new ArrayList<>();
    final List<Vertex> minimapPoints = new ArrayList<>();
    final List<Vertex> previewPoints = new ArrayList<>();

    public ChartData(String name, String color) {
        this.name = name;
        this.color = color;
    }


}
