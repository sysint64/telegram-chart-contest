package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Paint;
import android.graphics.Rect;
import ru.kabylin.andrey.telegramcontest.helpers.DateUtils;
import ru.kabylin.andrey.telegramcontest.helpers.MathUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartSolverImpl implements ChartSolver {
    private ChartState chartState;
    private long lastTime = System.nanoTime();

    public void setChartState(ChartState chartState) {
        this.chartState = chartState;
        fillAxisPoints(this.chartState.xValues);
    }

    private void fillAxisPoints(List<Long> xValues) {
        final long x0 = xValues.get(0);
        final float xLast = xValues.get(xValues.size() - 1);

        final int count = 256;
        final int delta = (int) Math.ceil((xLast - x0) / count);
        long x = x0;

        for (int i = 0; i < count; ++i) {
            chartState.xAxis.add(
                    new AxisVertex(
                            x,
                            0,
                            DateUtils.humanizeDate(new Date(x)).split(",")[0]
                    )
            );

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

    private void calculateChartMinimapPoints(final Rect rect, final float yMax, final ChartData chartData) {
        chartData.minimapPoints.clear();

        final long x0 = chartData.originalData.get(0).x;
        final long y0 = 0;

        final float xLast = chartData.originalData.get(chartData.originalData.size() - 1).x;

        for (int i = 0; i < chartData.originalData.size(); ++i) {
            final Vertex originalVertex = chartData.originalData.get(i);
            final Vertex minimapVertex = chartData.minimapPointsPool.get(i);

            projectVertex(originalVertex, minimapVertex, rect, x0, y0, xLast, yMax);
            chartData.minimapPoints.add(minimapVertex);
//        for (final Vertex vertex : chartData.originalData) {
//            chartData.minimapPoints.add(projectVertex(vertex, rect, x0, y0, xLast, yMax));
//            projectVertex(vertex, rect, x0, y0, xLast, yMax);
        }

        chartState.minimapRect = rect;
    }

    @Override
    public void calculatePreviewPoints(Rect rect) {
        final List<List<Vertex>> subCharts = new ArrayList<>();

        for (final ChartData chartData : chartState.charts) {
            if (chartData.isVisible) {
                final List<Vertex> points = getMinimapPreviewPoints(chartData);
                subCharts.add(points);
            }
        }

        chartState.statePreviewMaxY = findMaxByYInLists(subCharts);

        if (!chartState.isInitPreviewMaxY) {
            chartState.previewMaxY = chartState.statePreviewMaxY;
            chartState.isInitPreviewMaxY = true;
        }

        for (final ChartData chartData : chartState.charts) {
            // TODO: можно передать уже посчитанные точки методом `getMinimapPreviewPoints`
            calculatePreviewPoints(rect, chartState.previewMaxY, chartData);
        }
    }

    private void calculatePreviewPoints(final Rect rect, final float yMax, final ChartData chartData) {
        chartData.previewPoints.clear();
        final List<Vertex> points = getMinimapPreviewPoints(chartData);

        final long x0 = chartState.minimapPreviewLeft;
        final long y0 = chartState.minimapRect.bottom;

        final float xLast = chartState.minimapPreviewRight;

        for (final Vertex vertex : points) {
            chartData.previewPoints.add(projectVertex(vertex, rect, x0, y0, xLast, yMax));
        }
    }

    private Vertex projectVertex(Vertex vertex, Rect rect, float x0, float y0, float xMax, float yMax) {
        return new Vertex(
                projectX(vertex.x, rect, x0, xMax),
                projectY(vertex.y, rect, y0, yMax)
        );
    }

    private void projectVertex(Vertex vertex, Vertex to, Rect rect, float x0, float y0, float xMax, float yMax) {
        to.x = (long) projectX(vertex.x, rect, x0, xMax);
        to.y = (long) projectY(vertex.y, rect, y0, yMax);
    }

    private float projectX(float x, Rect rect, float x0, float xMax) {
        return rect.left + (x - x0) / (xMax - x0) * (rect.right - rect.left);
    }

    private float projectY(float y, Rect rect, float y0, float yMax) {
        return rect.bottom - (y / yMax) * (rect.bottom - rect.top);
    }

    private List<Vertex> getMinimapPreviewPoints(final ChartData chartData) {
        final ArrayList<Vertex> previewOriginalPoints = new ArrayList<>();
        Vertex prevPoint = null;
        boolean firstPointSet = false;

        for (int i = 0; i < chartData.originalData.size(); ++i) {
            final Vertex point = new Vertex(
                    chartData.minimapPoints.get(i).x,
                    chartData.originalData.get(i).y
            );
//            final Vertex point = chartData.minimapPointsPool

            if (point.x >= chartState.minimapPreviewLeft && point.x <= chartState.minimapPreviewRight) {
                // Add point before left border
                if (prevPoint != null && !firstPointSet) {
                    previewOriginalPoints.add(prevPoint);
                }

                previewOriginalPoints.add(point);
                firstPointSet = true;
            } else if (firstPointSet) {
                // Add point after right border
                previewOriginalPoints.add(point);
                break;
            } else {
                prevPoint = point;
            }
        }

        return previewOriginalPoints;
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
            final float max = findMaxByY(chartData).y;

            if (max > yMax) {
                yMax = max;
            }
        }

        return yMax;
    }

    private Vertex findMaxByY(List<Vertex> points) {
        Vertex max = null;

        for (final Vertex point : points) {
            if (max == null || max.y < point.y) {
                max = point;
            }
        }

        return max;
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
    public void onProgress() {
        final long time = System.nanoTime();
//        final float deltaTime = (time - lastTime) / 1000000f / 10000f;
        final float deltaTime = 0.005f;

        chartState.previewMaxY = MathUtils.interpTo(
                chartState.previewMaxY,
                chartState.statePreviewMaxY,
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

        lastTime = time;
    }

    @Override
    public boolean setChartVisibilityByName(String name, boolean visibility) {
        for (final ChartData chart : chartState.charts) {
            if (chart.name.equals(name)) {
                chart.isVisible = visibility;
                chart.stateOpacity = chart.isVisible ? 1f : 0f;
                return true;
            }
        }

        return false;
    }

    @Override
    public void calculateAxisXPoints(Rect rect) {
        chartState.previewAxisX.clear();
        final List<AxisVertex> vertices = getPreviewAxisVertices();

        if (vertices.isEmpty()) {
            return;
        }

        for (AxisVertex vertex : chartState.xAxis) {
            vertex.stateOpacity = 0f;
        }

        projectAxisXVertex(rect, vertices, 0, 1f);
        projectAxisXVertex(rect, vertices, vertices.size() - 1, 1f);

        fallThroughAxisX(rect, vertices, 0, vertices.size() - 1);
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
        projectAxisXVertex(rect, vertex, stateOpacity);
    }

    private void projectAxisXVertex(Rect rect, AxisVertex vertex, float stateOpacity) {
        vertex.original.stateOpacity = stateOpacity;
        chartState.previewAxisX.add(
                new AxisVertex(
                        (int) projectValueAxisXVertex(rect, vertex),
                        (int) (rect.bottom + chartState.axisXOffsetY),
                        vertex.title,
                        stateOpacity,
                        vertex.opacity,
                        vertex.original
                )
        );
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

    private List<AxisVertex> getPreviewAxisVertices() {
        final List<AxisVertex> vertices = new ArrayList<>();

        final long x0 = chartState.xAxis.get(0).x;
        final float xMax = chartState.xAxis.get(chartState.xAxis.size() - 1).x;

        for (final AxisVertex vertex : chartState.xAxis) {
            final float minimapX = projectX(vertex.x, chartState.minimapRect, x0, xMax);

            vertices.add(
                    new AxisVertex(
                            (long) minimapX,
                            vertex.y,
                            vertex.title,
                            vertex.stateOpacity,
                            vertex.opacity,
                            vertex
                    )
            );
        }

        return vertices;
    }

    @Override
    public void calculateAxisYPoints(Rect rect) {
        if (chartState.yAxisCurrent.isEmpty()) {
            for (int i = 0; i < 5; ++i) {
                chartState.yAxisCurrent.add(new AxisVertex(0, 0, "-", 0f, 0f, null, 0f, 0f));
                chartState.yAxisPast.add(new AxisVertex(0, 0, "-", 0f, 0f, null, 0f, 0f));
            }

            chartState.yAxis.clear();
            chartState.yAxis.addAll(chartState.yAxisCurrent);
            chartState.yAxis.addAll(chartState.yAxisPast);
        }

        if (Math.floor(chartState.lastStatePreviewMaxY) != Math.floor(chartState.statePreviewMaxY)) {
            float deltaMaxY = ((rect.bottom - rect.top) / 5f) * ((chartState.lastStatePreviewMaxY - chartState.statePreviewMaxY) / Math.max(chartState.lastStatePreviewMaxY, chartState.statePreviewMaxY));
            deltaMaxY = Math.max(deltaMaxY, chartState.minAxisXDelta);

            for (int i = 0; i < chartState.yAxisPast.size(); ++i) {
                AxisVertex vertex = chartState.yAxisPast.get(i);
                AxisVertex current = chartState.yAxisCurrent.get(i);

                vertex.x = current.x;
                vertex.y = current.y;

                if (vertex.opacity < 0.01f) {
                    vertex.yOffset = 0f;
                    vertex.stateYOffset = -deltaMaxY;
                    vertex.title = current.title;
                } else {
                    vertex.stateYOffset -= deltaMaxY;
                }

                vertex.stateOpacity = 0f;
                vertex.opacity = 1f;
            }

            final long delta = (int) ((rect.bottom - rect.top - chartState.axisYTopPadding) / 5f);
            final long deltaValue = (long) (chartState.statePreviewMaxY / 6f);

            long current = rect.bottom;
            long currentValue = 0;

            for (AxisVertex vertex : chartState.yAxisCurrent) {
                current -= delta;
                currentValue += deltaValue;

                if (vertex.opacity > 0.9f) {
                    vertex.yOffset = deltaMaxY;
                    vertex.stateYOffset = 0f;
                } else {
                    vertex.stateYOffset = 0f;
                    vertex.yOffset += deltaMaxY;
                }

                vertex.x = (int) (rect.left + chartState.axisYTextOffsetX);
                vertex.y = current;
                vertex.opacity = 0f;
                vertex.stateOpacity = 1f;
                vertex.title = String.valueOf(currentValue);
            }
        }

        chartState.lastStatePreviewMaxY = chartState.statePreviewMaxY;

        chartState.previewAxisY.clear();
        chartState.previewAxisY.add(
                new AxisVertex(
                        (int) (rect.left + chartState.axisYTextOffsetX),
                        rect.bottom,
                        "0",
                        1f,
                        1f,
                        null
                )
        );

        chartState.previewAxisY.addAll(chartState.yAxisCurrent);
        chartState.previewAxisY.addAll(chartState.yAxisPast);
    }
}
