package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Rect;
import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

// TODO: Encapsulation
public final class ChartState {
    int minimapPreviewLeft = 100;
    int minimapPreviewRight = minimapPreviewLeft + 100;
    int minimapPreviewResizeAreaSize = 20;
    Rect minimapRect = null;
    boolean isInitPreviewMaxY = false;
    float statePreviewMaxY = 0f;
    float previewMaxY = 0f;
    float previewMaxYChangeSpeed = 150f;
    float opacityChangeSpeed = 150f;
    float minimapMaxYChangeSpeed = 150f;
    float axisXOpacityChangeSpeed = 50f;
    float axisYOpacityChangeSpeed = 150f;
    float axisYOffsetChangeSpeed = 150f;
    float axisXDistance = MeasureUtils.convertDpToPixel(240);
    float axisXOffsetY = MeasureUtils.convertDpToPixel(20);
    float axisYTextOffsetX = MeasureUtils.convertDpToPixel(8);
    float axisYTextOffsetY = MeasureUtils.convertDpToPixel(5);
    float axisYTopPadding = MeasureUtils.convertDpToPixel(40);
    float minAxisXDelta = MeasureUtils.convertDpToPixel(10);

    List<ChartData> charts = new ArrayList<>();

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

        for (int i = 0; i < xValues.size(); ++i) {
            chart.originalData.add(new Vertex(xValues.get(i), y.get(i)));
            chart.minimapPointsPool.add(new Vertex(0, 0));
            chart.previewPointsPool.add(new Vertex(0, 0));
            chart.minimapInnerPreviewPool.add(new Vertex(0, 0));
        }

        charts.add(chart);
    }
}
