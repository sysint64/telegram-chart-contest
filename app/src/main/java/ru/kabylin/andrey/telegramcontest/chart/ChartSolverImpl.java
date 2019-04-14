package ru.kabylin.andrey.telegramcontest.chart;

import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Rect;

import org.json.JSONException;

import ru.kabylin.andrey.telegramcontest.helpers.DateHelper;
import ru.kabylin.andrey.telegramcontest.helpers.MathUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChartSolverImpl implements ChartSolver {
    private ChartState chartState;
    private ChartState zoomedState = null;
    private DataProvider dataProvider = new JsonDataProvider();
    private Vertex selectedVertex = null;
    private Vertex lastLeftBorderVertex = null;
    private Vertex lastRightBorderVertex = null;
    private OnChangeChartVisibleRangeListener onChangeChartVisibleRangeListener = null;

    private final List<List<Vertex>> subCharts = new ArrayList<>();
    private final ArrayList<ArrayList<Vertex>> previewOriginalPoints = new ArrayList<>();

    public void setChartState(ChartState chartState) {
        this.chartState = chartState;

        if (!this.chartState.isInit) {
            this.chartState.isInit = true;
            fillAxisPoints(this.chartState.xValues);
        }

        previewOriginalPoints.clear();
        chartState.popupIntersectPoints.clear();

        for (final ChartData chart : chartState.charts) {
            previewOriginalPoints.add(new ArrayList<Vertex>());
            chartState.popupIntersectPoints.add(new Vertex());
        }
    }

    @Override
    public void setOnChangeChartVisibleRangeListener(OnChangeChartVisibleRangeListener onChangeChartVisibleRangeListener) {
        this.onChangeChartVisibleRangeListener = onChangeChartVisibleRangeListener;
    }

    private void fillAxisPoints(List<Long> xValues) {
        final long x0 = xValues.get(0);
        final float xLast = xValues.get(xValues.size() - 1);

        final int count = 256;
        final int delta = (int) Math.ceil((xLast - x0) / count);
        long x = x0;

        for (int i = 0; i < count; ++i) {
            final String title;

            if (chartState.showTime) {
                title = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(x));
            } else {
                title = DateHelper.humanizeDate(new Date(x), false);
            }

            chartState.xAxis.add(new AxisVertex(x, 0, title));
            chartState.axisXPool.add(new AxisVertex(x, 0, title));
            chartState.previewAxisXPool.add(new AxisVertex(x, 0, title));

            x += delta;
        }

        chartState.xAxis.get(0).textAlign = Paint.Align.LEFT;
        chartState.xAxis.get(chartState.xAxis.size() - 1).textAlign = Paint.Align.RIGHT;
    }

    @Override
    public void calculateMinimapPoints(final Rect rect) {
        chartState.minimapRect = rect;
        findMaxByYInCharts(chartState.charts);

        for (final ChartData chartData : chartState.charts) {
            calculateChartMinimapPoints(rect, chartData.minimapMaxY, chartData);
        }
    }

    @Override
    public void calculate2YMinimapPoints(Rect rect) {
        if (chartState.minimapRect == null) {
            chartState.minimapRect = rect;
            // it'll clamp automatically to border
            setMinimapPosition(9999999);
        } else {
            chartState.minimapRect = rect;
        }

        for (final ChartData chartData : chartState.charts) {
            final float max = findMaxByY(chartData.originalData).y;
            calculateChartMinimapPoints(rect, max, chartData);
        }
    }

    private void calculateChartMinimapPoints(final Rect rect, final float yMax, final ChartData chartData) {
        chartData.minimapPoints.clear();

        final long x0 = (long) (chartData.originalData.get(0).x);
        final long y0 = 0;

        final float xLast = chartData.originalData.get(chartData.originalData.size() - 1).x;

        for (int i = 0; i < chartData.originalData.size(); ++i) {
            final Vertex originalVertex = chartData.originalData.get(i);
            final Vertex minimapVertex = chartData.minimapPointsPool.get(i);

            final float offsetSize = chartState.chartsScale * (xLast - x0);
            float position = ((float) chartState.minimapPreviewLeft + (chartState.minimapPreviewSize() / 2f)) / ((float) chartState.minimapRect.width());

//            if (chartState.previewRect != null) {
//                position += ((float) chartState.popup.left) / (float) chartState.previewRect.width();
//            }

            projectVertex(originalVertex, minimapVertex, rect, x0 + (offsetSize * position), y0, xLast - (offsetSize) + (offsetSize * position), yMax, chartData.scale);
            chartData.minimapPoints.add(minimapVertex);
        }

        chartState.minimapRect = rect;
    }

    @Override
    public void calculatePreviewPoints(Rect rect) {
        chartState.previewRect = rect;
        subCharts.clear();

        for (int i = 0; i < chartState.charts.size(); ++i) {
            final ChartData chartData = chartState.charts.get(i);

            if (chartData.isVisible) {
                final List<Vertex> points = getMinimapPreviewPoints(chartData, i, true);
                subCharts.add(points);
            }
        }

        if (chartState.normilizeByMin) {
            chartState.statePreviewMinY = findMinByYInLists(subCharts);
            chartState.statePreviewMinY2 = chartState.statePreviewMinY;
        }

        chartState.statePreviewMaxY = findMaxByYInLists(subCharts);

        if (chartState.statePreviewMaxY - chartState.statePreviewMinY < 0) {
            chartState.statePreviewMinY = 0;
        } else {
            chartState.statePreviewMaxY -= chartState.statePreviewMinY;
        }

        chartState.statePreviewMaxY2 = chartState.statePreviewMaxY;

        if (!chartState.isInitPreviewMinMaxY) {
            chartState.previewMaxY = chartState.statePreviewMaxY;
            chartState.previewMinY = chartState.statePreviewMinY;
            chartState.isInitPreviewMinMaxY = true;
        }

        for (int i = 0; i < chartState.charts.size(); ++i) {
            final ChartData chartData = chartState.charts.get(i);
            calculatePreviewPoints(rect, chartState.previewMaxY, chartState.previewMinY, chartData, i);
        }
    }

    @Override
    public void calculate2YPreviewPoints(Rect rect) {
        chartState.previewRect = rect;
        subCharts.clear();

        for (int i = 0; i < chartState.charts.size(); ++i) {
            final ChartData chartData = chartState.charts.get(i);

            if (chartData.isVisible) {
                final List<Vertex> points = getMinimapPreviewPoints(chartData, i, true);
                subCharts.add(points);
            }
        }

        if (subCharts.size() == 0)
            return;

        if (subCharts.size() == 1) {
            chartState.statePreviewMaxY = findMaxByY(subCharts.get(0)).y;
            chartState.statePreviewMaxY2 = chartState.statePreviewMaxY;

            if (chartState.normilizeByMin) {
                chartState.statePreviewMinY = findMinByY(subCharts.get(0)).y;
                chartState.statePreviewMinY2 = chartState.statePreviewMinY;
            }
        } else {
            chartState.statePreviewMaxY = findMaxByY(subCharts.get(0)).y;
            chartState.statePreviewMaxY2 = findMaxByY(subCharts.get(1)).y;

            if (chartState.normilizeByMin) {
                chartState.statePreviewMinY = findMinByY(subCharts.get(0)).y;
                chartState.statePreviewMinY2 = findMinByY(subCharts.get(1)).y;
            }
        }

        if (chartState.statePreviewMaxY - chartState.statePreviewMinY < 0) {
            chartState.statePreviewMinY = 0;
        } else {
            chartState.statePreviewMaxY -= chartState.statePreviewMinY;
        }

        if (chartState.statePreviewMaxY2 - chartState.statePreviewMinY2 < 0) {
            chartState.statePreviewMinY2 = 0;
        } else {
            chartState.statePreviewMaxY2 -= chartState.statePreviewMinY2;
        }

        if (!chartState.isInitPreviewMinMaxY) {
            chartState.previewMaxY = chartState.statePreviewMaxY;
            chartState.previewMaxY2 = chartState.statePreviewMaxY2;
            chartState.previewMinY = chartState.statePreviewMinY;
            chartState.previewMinY2 = chartState.statePreviewMinY2;
            chartState.isInitPreviewMinMaxY = true;
        }

        if (chartState.charts.size() != 2) {
            throw new AssertionError("Should be only 2 charts");
        }

        final ChartData chartData = chartState.charts.get(0);
        calculatePreviewPoints(rect, chartState.previewMaxY, chartState.previewMinY, chartData, 0);

        final ChartData chartData2 = chartState.charts.get(1);
        calculatePreviewPoints(rect, chartState.previewMaxY2, chartState.previewMinY2, chartData2, 1);
    }

    private void calculatePreviewPoints(final Rect rect, final float yMax, final float yMin, final ChartData chartData, final int index) {
        final List<Vertex> minimapPreviewPoints = getMinimapPreviewPoints(chartData, index, false);

        chartData.previewPoints.clear();

        final long x0 = chartState.minimapPreviewLeft;
        final float xLast = chartState.minimapPreviewRight;

        for (int i = 0; i < minimapPreviewPoints.size(); ++i) {
            final Vertex point = minimapPreviewPoints.get(i);
            final Vertex previewPoint = chartData.previewPointsPool.get(i);

            projectVertex(point, previewPoint, rect, x0, yMin, xLast, yMax, 1f);

            previewPoint.xValue = point.xValue;
            previewPoint.yValue = point.yValue;

            chartData.previewPoints.add(previewPoint);
        }

        Vertex leftBorderVertex = null;
        Vertex rightBorderVertex = null;

        if (!chartData.previewPoints.isEmpty()) {
            leftBorderVertex = chartData.previewPoints.get(0);
            rightBorderVertex = chartData.previewPoints.get(chartData.previewPoints.size() - 1);
        }

        if (lastLeftBorderVertex != leftBorderVertex || lastRightBorderVertex != rightBorderVertex) {
            String leftTitle = "?";
            String rightTitle = "?";

            if (leftBorderVertex != null) {
                leftTitle = leftBorderVertex.xValue;
            }

            if (rightBorderVertex != null) {
                rightTitle = rightBorderVertex.xValue;
            }

            chartState.rangeTitle = leftTitle + " - " + rightTitle;

            if (onChangeChartVisibleRangeListener != null && !leftTitle.equals("?") && !rightTitle.equals("?")) {
                onChangeChartVisibleRangeListener.onChangeChartVisibleRangeListener(leftTitle + " - " + rightTitle);
            }
        }

        lastLeftBorderVertex = leftBorderVertex;
        lastRightBorderVertex = rightBorderVertex;
    }

    private void projectVertex(Vertex vertex, Vertex to, Rect rect, float x0, float y0, float xMax, float yMax, float scale) {
        to.originalX = vertex.originalX;
        to.x = (long) projectX(vertex.x, rect, x0, xMax);
        to.y = (long) projectY(vertex.y * scale, rect, y0, yMax);
    }

    private float projectX(float x, Rect rect, float x0, float xMax) {
        return rect.left + (x - x0) / (xMax - x0) * (rect.right - rect.left);
    }

    private float projectY(float y, Rect rect, float y0, float yMax) {
        return rect.bottom - ((y - y0) / yMax) * (rect.bottom - rect.top);
    }

    private List<Vertex> getMinimapPreviewPoints(final ChartData chartData, final int previewOriginalPointIndex, final boolean ignoreBorder) {
        previewOriginalPoints.get(previewOriginalPointIndex).clear();

        Vertex prevPoint = null;
        boolean firstPointSet = false;

        for (int i = 0; i < chartData.originalData.size(); ++i) {
            final Vertex point = chartData.minimapInnerPreviewPool.get(i);

            point.originalX = chartData.originalData.get(i).originalX;
            point.x = chartData.minimapPoints.get(i).x;
            point.y = chartData.originalData.get(i).y * chartData.scale;

            if (point.x > chartState.minimapPreviewLeft && point.x < chartState.minimapPreviewRight) {
                // Add point before left border
                if (prevPoint != null && !firstPointSet && !ignoreBorder) {
                    previewOriginalPoints.get(previewOriginalPointIndex).add(prevPoint);
                }

                previewOriginalPoints.get(previewOriginalPointIndex).add(point);
                firstPointSet = true;
            } else if (firstPointSet) {
                // Add point after right border
                if (!ignoreBorder) {
                    previewOriginalPoints.get(previewOriginalPointIndex).add(point);
                }
                break;
            } else {
                prevPoint = point;
            }
        }

        return previewOriginalPoints.get(previewOriginalPointIndex);
    }

    private void findMaxByYInCharts(final List<ChartData> charts) {
        float yMax = 0;

        for (final ChartData chartData : charts) {
            if (chartData.isVisible) {
                final float max = findMaxByY(chartData.originalData).y;

                if (max > yMax) {
                    yMax = max;
                }
            }
        }

        for (final ChartData chartData : charts) {
            if (chartData.isVisible) {
                chartData.stateMinimapMaxY = yMax;

                if (!chartData.isMinimapMaxYInit) {
                    chartData.minimapMaxY = yMax;
                    chartData.isMinimapMaxYInit = true;
                }
            }
        }
    }

    private float findMaxByYInLists(final List<List<Vertex>> charts) {
        float yMax = 0;

        for (final List<Vertex> chartData : charts) {
            final Vertex vertex = findMaxByY(chartData);
            if (vertex == null) {
                continue;
            }
            final float max = vertex.y;

            if (max > yMax) {
                yMax = max;
            }
        }

        return yMax;
    }

    private Vertex findMaxByY(List<Vertex> points) {
        Vertex max = new Vertex();

        for (final Vertex point : points) {
            if (max.y < point.y) {
                max = point;
            }
        }

        return max;
    }

    private float findMinByYInLists(final List<List<Vertex>> charts) {
        float yMin = Float.MAX_VALUE;

        for (final List<Vertex> chartData : charts) {
            final Vertex vertex = findMinByY(chartData);
            if (vertex == null) {
                continue;
            }
            final float min = vertex.y;

            if (min < yMin) {
                yMin = min;
            }
        }

        return yMin;
    }

    private Vertex findMinByY(List<Vertex> points) {
        Vertex min = new Vertex();
        min.y = Float.MAX_VALUE;

        for (final Vertex point : points) {
            if (min.y > point.y) {
                min = point;
            }
        }

        return min;
    }

    @Override
    public ChartState getState() {
        return chartState;
    }

    @Override
    public void setMinimapPosition(float newPosition) {
        final int size = chartState.minimapPreviewSize();

        final Rect minimapRect = chartState.minimapRect;
        chartState.minimapPreviewLeft = MathUtils.clamp(
                (int) newPosition,
                minimapRect.left,
                minimapRect.right - size
        );

        setMinimapRight(chartState.minimapPreviewLeft + size);
    }

    @Override
    public void setMinimapLeft(float newLeft) {
        final Rect minimapRect = chartState.minimapRect;
        chartState.minimapPreviewLeft = MathUtils.clamp(
                (int) newLeft,
                minimapRect.left,
                chartState.minimapPreviewRight - chartState.minimapPreviewResizeAreaSize * 2
        );
    }

    @Override
    public void setMinimapRight(float newRight) {
        final Rect minimapRect = chartState.minimapRect;
        chartState.minimapPreviewRight = MathUtils.clamp(
                (int) newRight,
                chartState.minimapPreviewLeft + chartState.minimapPreviewResizeAreaSize * 2,
                minimapRect.right
        );
    }

    @Override
    public void onProgress(final float deltaTime) {
        chartState.previewMaxY = MathUtils.interpTo(
                chartState.previewMaxY,
                chartState.statePreviewMaxY,
                deltaTime,
                chartState.previewMaxYChangeSpeed
        );

        chartState.previewMaxY2 = MathUtils.interpTo(
                chartState.previewMaxY2,
                chartState.statePreviewMaxY2,
                deltaTime,
                chartState.previewMaxYChangeSpeed
        );

        chartState.previewMinY = MathUtils.interpTo(
                chartState.previewMinY,
                chartState.statePreviewMinY,
                deltaTime,
                chartState.previewMaxYChangeSpeed
        );

        chartState.previewMinY2 = MathUtils.interpTo(
                chartState.previewMinY2,
                chartState.statePreviewMinY2,
                deltaTime,
                chartState.previewMaxYChangeSpeed
        );

        for (final ChartData chart : chartState.charts) {
            chart.opacity = MathUtils.interpTo(
                    chart.opacity,
                    chart.stateOpacity,
                    deltaTime,
                    chartState.opacityChangeSpeed
            );

            chart.minimapMaxY = MathUtils.interpTo(
                    chart.minimapMaxY,
                    chart.stateMinimapMaxY,
                    deltaTime,
                    chartState.minimapMaxYChangeSpeed
            );

            if (chartState.chartType == ChartType.BARS) {
                chart.scale = MathUtils.interpTo(
                        chart.scale,
                        chart.stateScale,
                        deltaTime,
                        chartState.stackedScaleChangeSpeed
                );
            } else {
                chart.scale = MathUtils.interpTo(
                        chart.scale,
                        chart.stateScale,
                        deltaTime,
                        chartState.areaStackedScaleChangeSpeed
                );
            }
        }

        for (final AxisVertex vertex : chartState.xAxis) {
            vertex.opacity = MathUtils.interpTo(
                    vertex.opacity,
                    vertex.stateOpacity,
                    deltaTime,
                    chartState.axisXOpacityChangeSpeed
            );
        }

        for (final AxisVertex vertex : chartState.yAxis) {
            vertex.opacity = MathUtils.interpTo(
                    vertex.opacity,
                    vertex.stateOpacity,
                    deltaTime,
                    chartState.axisYOpacityChangeSpeed
            );

            vertex.yOffset = MathUtils.interpTo(
                    vertex.yOffset,
                    vertex.stateYOffset,
                    deltaTime,
                    chartState.axisYOffsetChangeSpeed
            );
        }

        for (final AxisVertex vertex : chartState.y2Axis) {
            vertex.opacity = MathUtils.interpTo(
                    vertex.opacity,
                    vertex.stateOpacity,
                    deltaTime,
                    chartState.axisYOpacityChangeSpeed
            );

            vertex.yOffset = MathUtils.interpTo(
                    vertex.yOffset,
                    vertex.stateYOffset,
                    deltaTime,
                    chartState.axisYOffsetChangeSpeed
            );
        }

        chartState.popup.opacity = MathUtils.interpTo(
                chartState.popup.opacity,
                chartState.popup.stateOpacity,
                deltaTime,
                chartState.popupOpacityChangeSpeed
        );

        chartState.chartsScale = MathUtils.linearInterpTo(
                chartState.chartsScale,
                chartState.chartsScaleState,
                deltaTime,
                chartState.zoomScaleChangeSpeed
        );

        chartState.chartsOpacity = MathUtils.linearInterpTo(
                chartState.chartsOpacity,
                chartState.chartsOpacityState,
                deltaTime,
                chartState.zoomOpacityChangeSpeed
        );

        chartState.chartsOpacity = MathUtils.clamp(chartState.chartsOpacity, 0.0f, 1.0f);
        chartState.chartsScale = MathUtils.clamp(chartState.chartsScale, 0.0f, 1.0f);

        if (chartState.normilizeToPercentage) {
            chartState.normalizeDataToPercentage();
        }
    }

    @Override
    public boolean setChartVisibilityByName(String name, boolean visibility) {
        for (final ChartData chart : chartState.charts) {
            if (chart.name.equals(name)) {
                chart.isVisible = visibility;

                if (chartState.chartType == ChartType.BARS) {
                    chart.stateScale = chart.isVisible ? 1f : 0f;
                } else if (chartState.chartType == ChartType.STACKED_AREA) {
                    chart.stateOpacity = chart.isVisible ? 1f : 0f;
                    chart.stateScale = chart.isVisible ? 1f : 0f;
                } else {
                    chart.stateOpacity = chart.isVisible ? 1f : 0f;
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void calculateAxisXPoints(Rect rect) {
        chartState.previewAxisX.clear();
        final List<AxisVertex> vertices = getPreviewAxisXVertices();

        if (vertices.isEmpty()) {
            return;
        }

        for (AxisVertex vertex : chartState.xAxis) {
            vertex.stateOpacity = 0f;
        }

        projectAxisXVertex(rect, vertices, 0, 1f);
        projectAxisXVertex(rect, vertices, vertices.size() - 1, 1f);

        fallThroughAxisX(rect, vertices, 0, vertices.size() - 1);

        chartState.axisXIsInit = true;
    }

    private void fallThroughAxisX(Rect rect, List<AxisVertex> vertices, int left, int right) {
        fallThroughAxisX(rect, vertices, left, right, false);
    }

    private void fallThroughAxisX(Rect rect, List<AxisVertex> vertices, int left, int right, boolean finishState) {
        if (right - left == 0) {
            return;
        }

        final float leftProjection = projectValueAxisXVertex(rect, vertices, left);
        final float rightProjection = projectValueAxisXVertex(rect, vertices, right);

        final int midl = left + (int) Math.floor((right - left) / 2f);
        final int midr = left + (int) Math.ceil((right - left) / 2f);

        if (finishState) {
            projectAxisXVertex(rect, vertices, midl, 0f);
            return;
        } else {
            projectAxisXVertex(rect, vertices, midl, 1f);
        }

        if (rightProjection - leftProjection > chartState.axisXDistance) {
            fallThroughAxisX(rect, vertices, left, midr);
            fallThroughAxisX(rect, vertices, midl, right);
        } else {
            fallThroughAxisX(rect, vertices, left, midr, true);
            fallThroughAxisX(rect, vertices, midl, right, true);
        }
    }

    private void projectAxisXVertex(Rect rect, List<AxisVertex> vertices, int index, float stateOpacity) {
        final AxisVertex vertex = vertices.get(index);
        projectAxisXVertex(rect, vertex, index, stateOpacity);
    }

    private void projectAxisXVertex(Rect rect, AxisVertex vertex, int index, float stateOpacity) {
        AxisVertex previewVertex = chartState.previewAxisXPool.get(index);

        vertex.original.stateOpacity = stateOpacity;

        if (!chartState.axisXIsInit) {
            vertex.original.opacity = stateOpacity;
        }

        previewVertex.x = (int) projectValueAxisXVertex(rect, vertex);
        previewVertex.y = (int) (rect.bottom + chartState.axisXOffsetY);
        previewVertex.title = vertex.title;
        previewVertex.stateOpacity = stateOpacity;
        previewVertex.opacity = vertex.opacity;
        previewVertex.original = vertex.original;

        chartState.previewAxisX.add(previewVertex);
    }

    private float projectValueAxisXVertex(Rect rect, List<AxisVertex> vertices, int index) {
        final AxisVertex vertex = vertices.get(index);
        return projectValueAxisXVertex(rect, vertex);
    }

    private float projectValueAxisXVertex(Rect rect, AxisVertex vertex) {
        final long x0 = chartState.minimapPreviewLeft;
        final float xMax = chartState.minimapPreviewRight;
        return projectX(vertex.x, rect, x0, xMax);
    }

    private List<AxisVertex> getPreviewAxisXVertices() {
        final List<AxisVertex> vertices = new ArrayList<>();

        final long x0 = chartState.xAxis.get(0).x;
        final float xMax = chartState.xAxis.get(chartState.xAxis.size() - 1).x;

        for (int i = 0; i < chartState.xAxis.size(); ++i) {
            final AxisVertex originalVertex = chartState.xAxis.get(i);
            AxisVertex previewVertex = chartState.axisXPool.get(i);
            final float minimapX = projectX(originalVertex.x, chartState.minimapRect, x0, xMax);

            previewVertex.x = (long) minimapX;
            previewVertex.y = originalVertex.y;
            previewVertex.title = originalVertex.title;
            previewVertex.stateOpacity = originalVertex.stateOpacity;
            previewVertex.opacity = originalVertex.opacity;
            previewVertex.original = originalVertex;

            vertices.add(previewVertex);
        }

        return vertices;
    }

    private final class CalculateAxisYPointsInput {
        final List<AxisVertex> yAxis;
        final List<AxisVertex> yAxisCurrent;
        final List<AxisVertex> yAxisPast;
        final float statePreviewMaxY;
        final float statePreviewMinY;
        final float lastStatePreviewMaxY;
        final List<AxisVertex> previewAxisY;
        final AxisVertex previewAxisYZero;
        final Paint.Align align;
        final boolean axisYIsInit;

        private CalculateAxisYPointsInput(
                List<AxisVertex> yAxis, List<AxisVertex> yAxisCurrent,
                List<AxisVertex> yAxisPast, float statePreviewMaxY,
                float statePreviewMinY, float lastStatePreviewMaxY, List<AxisVertex> previewAxisY,
                AxisVertex previewAxisYZero, Paint.Align align,
                boolean axisYIsInit
        ) {
            this.yAxis = yAxis;
            this.yAxisCurrent = yAxisCurrent;
            this.yAxisPast = yAxisPast;
            this.statePreviewMaxY = statePreviewMaxY;
            this.statePreviewMinY = statePreviewMinY;
            this.lastStatePreviewMaxY = lastStatePreviewMaxY;
            this.previewAxisY = previewAxisY;
            this.previewAxisYZero = previewAxisYZero;
            this.align = align;
            this.axisYIsInit = axisYIsInit;
        }
    }

    private boolean calculateAxisYPoints_(CalculateAxisYPointsInput input, Rect rect) {
        boolean isInit = input.axisYIsInit;

        if (input.yAxisCurrent.isEmpty()) {
            for (int i = 0; i < chartState.yAxisLines - 1; ++i) {
                input.yAxisCurrent.add(new AxisVertex(0, 0, "-", 0f, 0f, null, 0f, 0f));
                input.yAxisPast.add(new AxisVertex(0, 0, "-", 0f, 0f, null, 0f, 0f));
            }

            input.yAxis.clear();
            input.yAxis.addAll(input.yAxisCurrent);
            input.yAxis.addAll(input.yAxisPast);
        }

        if (Math.floor(input.lastStatePreviewMaxY) != Math.floor(input.statePreviewMaxY)) {
            float deltaMaxY = ((rect.bottom - rect.top) / (float) (chartState.yAxisLines - 1)) * ((input.lastStatePreviewMaxY - input.statePreviewMaxY) / Math.max(input.lastStatePreviewMaxY, input.statePreviewMaxY));

            if (Math.abs(deltaMaxY) < chartState.minAxisYDelta) {
                deltaMaxY = 0;
            }

            for (int i = 0; i < input.yAxisPast.size(); ++i) {
                AxisVertex vertex = input.yAxisPast.get(i);
                AxisVertex current = input.yAxisCurrent.get(i);

                vertex.x = current.x;
                vertex.y = current.y;

                if (input.axisYIsInit) {
                    if (vertex.opacity < 0.1f) {
                        vertex.yOffset = 0f;
                        vertex.stateYOffset = -deltaMaxY;
                        vertex.title = current.title;

                        vertex.opacity = 1f;
                        vertex.stateOpacity = 0f;
                    } else {
                        vertex.stateYOffset -= deltaMaxY;
                    }
                } else {
                    vertex.yOffset = 0f;
                    vertex.stateYOffset = 0f;
                    vertex.opacity = 0f;
                    vertex.stateOpacity = 0f;
                }
            }

            final long delta = Math.round((rect.bottom - rect.top) / (float) chartState.yAxisLines);
            final long deltaValue = Math.round((input.statePreviewMaxY / (float) chartState.yAxisLines));

            long current = rect.bottom;
            long currentValue = (long) input.statePreviewMinY;

            for (AxisVertex vertex : input.yAxisCurrent) {
                current -= delta;
                currentValue += deltaValue;

                if (input.axisYIsInit) {
                    if (vertex.opacity > 0.9f) {
                        vertex.yOffset = deltaMaxY;
                        vertex.stateYOffset = 0f;
                    } else {
                        vertex.yOffset += deltaMaxY;
                    }

                    vertex.opacity = 0f;
                    vertex.stateOpacity = 1f;
                } else {
                    vertex.yOffset = 0f;
                    vertex.stateYOffset = 0f;
                    vertex.opacity = 1f;
                    vertex.stateOpacity = 1f;
                }

                if (input.align == Paint.Align.LEFT) {
                    vertex.x = (int) (rect.left + chartState.axisYTextOffsetX);
                } else if (input.align == Paint.Align.RIGHT) {
                    vertex.x = (int) (rect.right - chartState.axisYTextOffsetX);
                }

                vertex.y = current;
                vertex.title = String.valueOf(currentValue);
            }

            isInit = true;
        }

        if (input.align == Paint.Align.LEFT) {
            input.previewAxisYZero.x = (int) (rect.left + chartState.axisYTextOffsetX);
        } else if (input.align == Paint.Align.RIGHT) {
            input.previewAxisYZero.x = (int) (rect.right - chartState.axisYTextOffsetX);
        }

        input.previewAxisYZero.y = rect.bottom;
        input.previewAxisYZero.title = String.valueOf((long) input.statePreviewMinY);
        input.previewAxisYZero.opacity = 1f;
        input.previewAxisYZero.stateOpacity = 1f;

        input.previewAxisY.clear();
        input.previewAxisY.add(input.previewAxisYZero);
        input.previewAxisY.addAll(input.yAxisCurrent);
        input.previewAxisY.addAll(input.yAxisPast);

        return isInit;
    }

    @Override
    public void calculateAxisYPoints(Rect rect) {
        chartState.axisYIsInit = calculateAxisYPoints_(
                new CalculateAxisYPointsInput(
                        chartState.yAxis,
                        chartState.yAxisCurrent,
                        chartState.yAxisPast,
                        chartState.statePreviewMaxY,
                        chartState.statePreviewMinY,
                        chartState.lastStatePreviewMaxY,
                        chartState.previewAxisY,
                        chartState.previewAxisYZero,
                        Paint.Align.LEFT,
                        chartState.axisYIsInit
                ),
                rect
        );

        chartState.axisY2IsInit = calculateAxisYPoints_(
                new CalculateAxisYPointsInput(
                        chartState.y2Axis,
                        chartState.y2AxisCurrent,
                        chartState.y2AxisPast,
                        chartState.statePreviewMaxY2,
                        chartState.statePreviewMinY2,
                        chartState.lastStatePreviewMaxY2,
                        chartState.previewAxisY2,
                        chartState.previewAxisY2Zero,
                        Paint.Align.RIGHT,
                        chartState.axisY2IsInit
                ),
                rect
        );

        chartState.lastStatePreviewMaxY = chartState.statePreviewMaxY;
        chartState.lastStatePreviewMaxY2 = chartState.statePreviewMaxY2;
    }

    @Override
    public void dropPopup(float touchX, float touchY) {
        final List<Popup.PopupItem> items = new ArrayList<>();
        long x = 0;
        String title = "";

        for (int i = 0; i < chartState.charts.size(); ++i) {
            final ChartData chart = chartState.charts.get(i);

            if (chart.isVisible) {
                final Vertex vertex = findVertexUnderTouch(chart.previewPoints, touchX, touchY);

                if (vertex != null) {
                    items.add(new Popup.PopupItem(vertex.color, vertex.yValue, vertex.title));
                    x = (long) vertex.x;
                    title = vertex.xValue;

                    Vertex intersectPoint = chartState.popupIntersectPoints.get(i);

                    intersectPoint.originalX = vertex.originalX;
                    intersectPoint.x = vertex.x;
                    intersectPoint.y = vertex.y;
                    chartState.intersectX = vertex.x;
                } else {
                    return;
                }
            }
        }

        chartState.popup.drop(title, x, 0, items);
    }

    private Vertex findVertexUnderTouch(List<Vertex> points, float touchX, float touchY) {
        for (final Vertex vertex : points) {
            final int halfRangeSize = (int) Math.ceil((float) chartState.previewRect.width() / (float) points.size() / 2f);

            if (touchX >= vertex.x - halfRangeSize && touchX <= vertex.x + halfRangeSize) {
                selectedVertex = vertex;
                return vertex;
            }
        }

        selectedVertex = null;
        return null;
    }

    @Override
    public void hidePopup() {
        chartState.popup.hide();
    }

    @Override
    public ChartState zoomIn(AssetManager assetManager) {
        if (selectedVertex == null) {
            return null;
        }

        chartState.zoomed = true;
        chartState.popup.hide();

        chartState.chartsScaleState = 1f;
        chartState.chartsOpacityState = 0f;

        try {
            zoomedState = dataProvider.getZoomed(assetManager, chartState.chartIndex, selectedVertex.originalX);

            zoomedState.chartsOpacity = 0f;
            zoomedState.chartsOpacityState = 0f;
            zoomedState.chartsScale = 1f;
            zoomedState.chartsScaleState = 1f;
            zoomedState.minimapPreviewLeft = chartState.minimapRect.width() / 2 - zoomedState.minimapPreviewSize() / 2;
            zoomedState.minimapPreviewRight = zoomedState.minimapPreviewLeft + zoomedState.minimapInitialPreviewSize;

            for (ChartData chart : chartState.charts) {
                for (ChartData zoomedChart : zoomedState.charts) {
                    if (zoomedChart.name.equals(chart.name)) {
                        zoomedChart.isVisible = chart.isVisible;
                        zoomedChart.stateOpacity = chart.stateOpacity;
                        zoomedChart.stateScale = zoomedChart.isVisible ? 1f : 0f;
                        zoomedChart.scale = zoomedChart.stateScale;
                        break;
                    }
                }
            }

            chartState.zoomedChartState = zoomedState;
            return zoomedState;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void zoomIn() {
        chartState.zoomed = true;
        chartState.popup.hide();
        chartState.chartsScaleState = 1f;
        chartState.chartsOpacityState = 0f;
    }

    @Override
    public void zoomOut() {
        chartState.zoomed = false;
        chartState.popup.hide();
        chartState.chartsScaleState = 0f;
        chartState.chartsOpacityState = 1f;

        if (zoomedState == null) {
            return;
        }

        for (final ChartData chart : chartState.charts) {
            for (final ChartData zoomedChart : zoomedState.charts) {
                if (chart.name.equals(zoomedChart.name)) {
                    chart.isVisible = zoomedChart.isVisible;
                    chart.stateOpacity = zoomedChart.stateOpacity;
                    chart.stateScale = zoomedChart.stateScale;
                    break;
                }
            }

            for (final ChartButton chartButton : chartState.buttons) {
                if (chartButton.title.equals(chart.name)) {
                    chartButton.isInit = false;
                    chartButton.isChecked = chart.isVisible;
                }
            }
        }
    }
}
