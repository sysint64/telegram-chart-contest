package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Color;
import android.graphics.Rect;
import ru.kabylin.andrey.telegramcontest.helpers.DateUtils;
import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ChartState {
    int minimapPreviewLeft = 100;
    int minimapPreviewRight = minimapPreviewLeft + 100;
    int minimapPreviewResizeAreaSize = 20;
    Rect minimapRect = null;
    Rect previewRect = null;
    boolean isInitPreviewMaxY = false;
    float statePreviewMaxY = 0f;
    float previewMaxY = 0f;

    float previewMaxYChangeSpeed = 150f;
    float opacityChangeSpeed = 150f;
    float minimapMaxYChangeSpeed = 150f;
    float axisXOpacityChangeSpeed = 50f;
    float axisYOpacityChangeSpeed = 150f;
    float axisYOffsetChangeSpeed = 150f;
    float popupOpacityChangeSpeed = 150f;

    float axisXDistance = MeasureUtils.convertDpToPixel(240);
    float axisXOffsetY = MeasureUtils.convertDpToPixel(20);
    float axisYTextOffsetX = MeasureUtils.convertDpToPixel(8);
    float axisYTextOffsetY = MeasureUtils.convertDpToPixel(5);
    float axisYTopPadding = MeasureUtils.convertDpToPixel(40);

    boolean axisXIsInit = false;
    boolean axisYIsInit = false;

    float minAxisYDelta = MeasureUtils.convertDpToPixel(10);
    float intersectPointSize = MeasureUtils.convertDpToPixel(4);
    float intersectPointStrokeWidth = MeasureUtils.convertDpToPixel(2);

    List<ChartData> charts = new ArrayList<>();
    List<Vertex> popupIntersectPoints = new ArrayList<>();

    final List<AxisVertex> xAxis = new ArrayList<>();

    final List<AxisVertex> yAxis = new ArrayList<>();
    final List<AxisVertex> yAxisCurrent = new ArrayList<>();
    final List<AxisVertex> yAxisPast = new ArrayList<>();

    // For Y axis animations
    float lastStatePreviewMaxY = 0f;

    final List<AxisVertex> previewAxisX = new ArrayList<>();
    final List<AxisVertex> previewAxisY = new ArrayList<>();

    final AxisVertex previewAxisYZero = new AxisVertex(0, 0, "0");

    final List<AxisVertex> axisXPool = new ArrayList<>();
    final List<AxisVertex> previewAxisXPool = new ArrayList<>();

    final List<Long> xValues = new ArrayList<>();

    final Popup popup = new Popup();

    boolean isInit = false;

    Rect getMinimapPreviewRect() {
        return new Rect(
                minimapPreviewLeft,
                minimapRect.top,
                minimapPreviewRight,
                minimapRect.bottom
        );
    }

    int minimapPreviewSize() {
        return minimapPreviewRight - minimapPreviewLeft;
    }

    public void setX(List<Long> x) {
        if (!xValues.isEmpty()) {
            throw new AssertionError("xValues already set");
        }

        this.xValues.addAll(x);
    }

    public void addChart(String color, String name, List<Long> y) {
        if (xValues.size() != y.size()) {
            throw new AssertionError(xValues.size() + " != " + y.size());
        }

        final ChartData chart = new ChartData(name, color);
        final int parsedColor = Color.parseColor(color);

        for (int i = 0; i < xValues.size(); ++i) {
            final String yValue = y.get(i).toString();
            final String xValue = DateUtils.humanizeDate(new Date(xValues.get(i)));

            chart.originalData.add(new Vertex(xValues.get(i), y.get(i), name, xValue, yValue, parsedColor));
            chart.minimapPointsPool.add(new Vertex(0, 0, name, xValue, yValue, parsedColor));
            chart.previewPointsPool.add(new Vertex(0, 0, name, xValue, yValue, parsedColor));
            chart.minimapInnerPreviewPool.add(new Vertex(0, 0, name, xValue, yValue, parsedColor));
        }

        charts.add(chart);
    }
}
