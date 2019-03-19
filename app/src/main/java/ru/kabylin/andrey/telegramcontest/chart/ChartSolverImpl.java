package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Paint;
import android.graphics.Rect;
import ru.kabylin.andrey.telegramcontest.helpers.DateUtils;
import ru.kabylin.andrey.telegramcontest.helpers.MathUtils;
import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartSolverImpl implements ChartSolver {
    private final ChartState chartState;
    private long lastTime = System.nanoTime();

    ChartSolverImpl() {
        final List<Vertex> chart0Points = new ArrayList<>();
        final List<Vertex> chart1Points = new ArrayList<>();

        chart0Points.add(new Vertex(1542412800000L, 37L));
        chart0Points.add(new Vertex(1542499200000L, 20L));
        chart0Points.add(new Vertex(1542585600000L, 32L));
        chart0Points.add(new Vertex(1542672000000L, 39L));
        chart0Points.add(new Vertex(1542758400000L, 32L));
        chart0Points.add(new Vertex(1542844800000L, 35L));
        chart0Points.add(new Vertex(1542931200000L, 19L));
        chart0Points.add(new Vertex(1543017600000L, 65L));
        chart0Points.add(new Vertex(1543104000000L, 36L));
        chart0Points.add(new Vertex(1543190400000L, 62L));
        chart0Points.add(new Vertex(1543276800000L, 113L));
        chart0Points.add(new Vertex(1543363200000L, 69L));
        chart0Points.add(new Vertex(1543449600000L, 120L));
        chart0Points.add(new Vertex(1543536000000L, 60L));
        chart0Points.add(new Vertex(1543622400000L, 51L));
        chart0Points.add(new Vertex(1543708800000L, 49L));
        chart0Points.add(new Vertex(1543795200000L, 71L));
        chart0Points.add(new Vertex(1543881600000L, 122L));
        chart0Points.add(new Vertex(1543968000000L, 149L));
        chart0Points.add(new Vertex(1544054400000L, 69L));
        chart0Points.add(new Vertex(1544140800000L, 57L));
        chart0Points.add(new Vertex(1544227200000L, 21L));
        chart0Points.add(new Vertex(1544313600000L, 33L));
        chart0Points.add(new Vertex(1544400000000L, 55L));
        chart0Points.add(new Vertex(1544486400000L, 92L));
        chart0Points.add(new Vertex(1544572800000L, 62L));
        chart0Points.add(new Vertex(1544659200000L, 47L));
        chart0Points.add(new Vertex(1544745600000L, 50L));
        chart0Points.add(new Vertex(1544832000000L, 56L));
        chart0Points.add(new Vertex(1544918400000L, 116L));
        chart0Points.add(new Vertex(1545004800000L, 63L));
        chart0Points.add(new Vertex(1545091200000L, 60L));
        chart0Points.add(new Vertex(1545177600000L, 55L));
        chart0Points.add(new Vertex(1545264000000L, 65L));
        chart0Points.add(new Vertex(1545350400000L, 76L));
        chart0Points.add(new Vertex(1545436800000L, 33L));
        chart0Points.add(new Vertex(1545523200000L, 45L));
        chart0Points.add(new Vertex(1545609600000L, 64L));
        chart0Points.add(new Vertex(1545696000000L, 54L));
        chart0Points.add(new Vertex(1545782400000L, 81L));
        chart0Points.add(new Vertex(1545868800000L, 180L));
        chart0Points.add(new Vertex(1545955200000L, 123L));
        chart0Points.add(new Vertex(1546041600000L, 106L));
        chart0Points.add(new Vertex(1546128000000L, 37L));
        chart0Points.add(new Vertex(1546214400000L, 60L));
        chart0Points.add(new Vertex(1546300800000L, 70L));
        chart0Points.add(new Vertex(1546387200000L, 46L));
        chart0Points.add(new Vertex(1546473600000L, 68L));
        chart0Points.add(new Vertex(1546560000000L, 46L));
        chart0Points.add(new Vertex(1546646400000L, 51L));
        chart0Points.add(new Vertex(1546732800000L, 33L));
        chart0Points.add(new Vertex(1546819200000L, 57L));
        chart0Points.add(new Vertex(1546905600000L, 75L));
        chart0Points.add(new Vertex(1546992000000L, 70L));
        chart0Points.add(new Vertex(1547078400000L, 95L));
        chart0Points.add(new Vertex(1547164800000L, 70L));
        chart0Points.add(new Vertex(1547251200000L, 50L));
        chart0Points.add(new Vertex(1547337600000L, 68L));
        chart0Points.add(new Vertex(1547424000000L, 63L));
        chart0Points.add(new Vertex(1547510400000L, 66L));
        chart0Points.add(new Vertex(1547596800000L, 53L));
        chart0Points.add(new Vertex(1547683200000L, 38L));
        chart0Points.add(new Vertex(1547769600000L, 52L));
        chart0Points.add(new Vertex(1547856000000L, 109L));
        chart0Points.add(new Vertex(1547942400000L, 121L));
        chart0Points.add(new Vertex(1548028800000L, 53L));
        chart0Points.add(new Vertex(1548115200000L, 36L));
        chart0Points.add(new Vertex(1548201600000L, 71L));
        chart0Points.add(new Vertex(1548288000000L, 96L));
        chart0Points.add(new Vertex(1548374400000L, 55L));
        chart0Points.add(new Vertex(1548460800000L, 58L));
        chart0Points.add(new Vertex(1548547200000L, 29L));
        chart0Points.add(new Vertex(1548633600000L, 31L));
        chart0Points.add(new Vertex(1548720000000L, 55L));
        chart0Points.add(new Vertex(1548806400000L, 52L));
        chart0Points.add(new Vertex(1548892800000L, 44L));
        chart0Points.add(new Vertex(1548979200000L, 126L));
        chart0Points.add(new Vertex(1549065600000L, 191L));
        chart0Points.add(new Vertex(1549152000000L, 73L));
        chart0Points.add(new Vertex(1549238400000L, 87L));
        chart0Points.add(new Vertex(1549324800000L, 255L));
        chart0Points.add(new Vertex(1549411200000L, 278L));
        chart0Points.add(new Vertex(1549497600000L, 219L));
        chart0Points.add(new Vertex(1549584000000L, 170L));
        chart0Points.add(new Vertex(1549670400000L, 129L));
        chart0Points.add(new Vertex(1549756800000L, 125L));
        chart0Points.add(new Vertex(1549843200000L, 126L));
        chart0Points.add(new Vertex(1549929600000L, 84L));
        chart0Points.add(new Vertex(1550016000000L, 65L));
        chart0Points.add(new Vertex(1550102400000L, 53L));
        chart0Points.add(new Vertex(1550188800000L, 154L));
        chart0Points.add(new Vertex(1550275200000L, 57L));
        chart0Points.add(new Vertex(1550361600000L, 71L));
        chart0Points.add(new Vertex(1550448000000L, 64L));
        chart0Points.add(new Vertex(1550534400000L, 75L));
        chart0Points.add(new Vertex(1550620800000L, 72L));
        chart0Points.add(new Vertex(1550707200000L, 39L));
        chart0Points.add(new Vertex(1550793600000L, 47L));
        chart0Points.add(new Vertex(1550880000000L, 52L));
        chart0Points.add(new Vertex(1550966400000L, 73L));
        chart0Points.add(new Vertex(1551052800000L, 89L));
        chart0Points.add(new Vertex(1551139200000L, 156L));
        chart0Points.add(new Vertex(1551225600000L, 86L));
        chart0Points.add(new Vertex(1551312000000L, 105L));
        chart0Points.add(new Vertex(1551398400000L, 88L));
        chart0Points.add(new Vertex(1551484800000L, 45L));
        chart0Points.add(new Vertex(1551571200000L, 33L));
        chart0Points.add(new Vertex(1551657600000L, 56L));
        chart0Points.add(new Vertex(1551744000000L, 142L));
        chart0Points.add(new Vertex(1551830400000L, 124L));
        chart0Points.add(new Vertex(1551916800000L, 114L));
        chart0Points.add(new Vertex(1552003200000L, 64L));

        chart1Points.add(new Vertex(1542412800000L, 22L));
        chart1Points.add(new Vertex(1542499200000L, 12L));
        chart1Points.add(new Vertex(1542585600000L, 30L));
        chart1Points.add(new Vertex(1542672000000L, 40L));
        chart1Points.add(new Vertex(1542758400000L, 33L));
        chart1Points.add(new Vertex(1542844800000L, 23L));
        chart1Points.add(new Vertex(1542931200000L, 18L));
        chart1Points.add(new Vertex(1543017600000L, 41L));
        chart1Points.add(new Vertex(1543104000000L, 45L));
        chart1Points.add(new Vertex(1543190400000L, 69L));
        chart1Points.add(new Vertex(1543276800000L, 57L));
        chart1Points.add(new Vertex(1543363200000L, 61L));
        chart1Points.add(new Vertex(1543449600000L, 70L));
        chart1Points.add(new Vertex(1543536000000L, 47L));
        chart1Points.add(new Vertex(1543622400000L, 31L));
        chart1Points.add(new Vertex(1543708800000L, 34L));
        chart1Points.add(new Vertex(1543795200000L, 40L));
        chart1Points.add(new Vertex(1543881600000L, 55L));
        chart1Points.add(new Vertex(1543968000000L, 27L));
        chart1Points.add(new Vertex(1544054400000L, 57L));
        chart1Points.add(new Vertex(1544140800000L, 48L));
        chart1Points.add(new Vertex(1544227200000L, 32L));
        chart1Points.add(new Vertex(1544313600000L, 40L));
        chart1Points.add(new Vertex(1544400000000L, 49L));
        chart1Points.add(new Vertex(1544486400000L, 54L));
        chart1Points.add(new Vertex(1544572800000L, 49L));
        chart1Points.add(new Vertex(1544659200000L, 34L));
        chart1Points.add(new Vertex(1544745600000L, 51L));
        chart1Points.add(new Vertex(1544832000000L, 51L));
        chart1Points.add(new Vertex(1544918400000L, 51L));
        chart1Points.add(new Vertex(1545004800000L, 66L));
        chart1Points.add(new Vertex(1545091200000L, 51L));
        chart1Points.add(new Vertex(1545177600000L, 94L));
        chart1Points.add(new Vertex(1545264000000L, 60L));
        chart1Points.add(new Vertex(1545350400000L, 64L));
        chart1Points.add(new Vertex(1545436800000L, 28L));
        chart1Points.add(new Vertex(1545523200000L, 44L));
        chart1Points.add(new Vertex(1545609600000L, 96L));
        chart1Points.add(new Vertex(1545696000000L, 49L));
        chart1Points.add(new Vertex(1545782400000L, 73L));
        chart1Points.add(new Vertex(1545868800000L, 30L));
        chart1Points.add(new Vertex(1545955200000L, 88L));
        chart1Points.add(new Vertex(1546041600000L, 63L));
        chart1Points.add(new Vertex(1546128000000L, 42L));
        chart1Points.add(new Vertex(1546214400000L, 56L));
        chart1Points.add(new Vertex(1546300800000L, 67L));
        chart1Points.add(new Vertex(1546387200000L, 52L));
        chart1Points.add(new Vertex(1546473600000L, 67L));
        chart1Points.add(new Vertex(1546560000000L, 35L));
        chart1Points.add(new Vertex(1546646400000L, 61L));
        chart1Points.add(new Vertex(1546732800000L, 40L));
        chart1Points.add(new Vertex(1546819200000L, 55L));
        chart1Points.add(new Vertex(1546905600000L, 63L));
        chart1Points.add(new Vertex(1546992000000L, 61L));
        chart1Points.add(new Vertex(1547078400000L, 105L));
        chart1Points.add(new Vertex(1547164800000L, 59L));
        chart1Points.add(new Vertex(1547251200000L, 51L));
        chart1Points.add(new Vertex(1547337600000L, 76L));
        chart1Points.add(new Vertex(1547424000000L, 63L));
        chart1Points.add(new Vertex(1547510400000L, 57L));
        chart1Points.add(new Vertex(1547596800000L, 47L));
        chart1Points.add(new Vertex(1547683200000L, 56L));
        chart1Points.add(new Vertex(1547769600000L, 51L));
        chart1Points.add(new Vertex(1547856000000L, 98L));
        chart1Points.add(new Vertex(1547942400000L, 103L));
        chart1Points.add(new Vertex(1548028800000L, 62L));
        chart1Points.add(new Vertex(1548115200000L, 54L));
        chart1Points.add(new Vertex(1548201600000L, 74L));
        chart1Points.add(new Vertex(1548288000000L, 48L));
        chart1Points.add(new Vertex(1548374400000L, 41L));
        chart1Points.add(new Vertex(1548460800000L, 41L));
        chart1Points.add(new Vertex(1548547200000L, 37L));
        chart1Points.add(new Vertex(1548633600000L, 30L));
        chart1Points.add(new Vertex(1548720000000L, 28L));
        chart1Points.add(new Vertex(1548806400000L, 26L));
        chart1Points.add(new Vertex(1548892800000L, 37L));
        chart1Points.add(new Vertex(1548979200000L, 65L));
        chart1Points.add(new Vertex(1549065600000L, 86L));
        chart1Points.add(new Vertex(1549152000000L, 70L));
        chart1Points.add(new Vertex(1549238400000L, 81L));
        chart1Points.add(new Vertex(1549324800000L, 54L));
        chart1Points.add(new Vertex(1549411200000L, 74L));
        chart1Points.add(new Vertex(1549497600000L, 70L));
        chart1Points.add(new Vertex(1549584000000L, 50L));
        chart1Points.add(new Vertex(1549670400000L, 74L));
        chart1Points.add(new Vertex(1549756800000L, 79L));
        chart1Points.add(new Vertex(1549843200000L, 85L));
        chart1Points.add(new Vertex(1549929600000L, 62L));
        chart1Points.add(new Vertex(1550016000000L, 36L));
        chart1Points.add(new Vertex(1550102400000L, 46L));
        chart1Points.add(new Vertex(1550188800000L, 68L));
        chart1Points.add(new Vertex(1550275200000L, 43L));
        chart1Points.add(new Vertex(1550361600000L, 66L));
        chart1Points.add(new Vertex(1550448000000L, 50L));
        chart1Points.add(new Vertex(1550534400000L, 28L));
        chart1Points.add(new Vertex(1550620800000L, 66L));
        chart1Points.add(new Vertex(1550707200000L, 39L));
        chart1Points.add(new Vertex(1550793600000L, 23L));
        chart1Points.add(new Vertex(1550880000000L, 63L));
        chart1Points.add(new Vertex(1550966400000L, 74L));
        chart1Points.add(new Vertex(1551052800000L, 83L));
        chart1Points.add(new Vertex(1551139200000L, 66L));
        chart1Points.add(new Vertex(1551225600000L, 40L));
        chart1Points.add(new Vertex(1551312000000L, 60L));
        chart1Points.add(new Vertex(1551398400000L, 29L));
        chart1Points.add(new Vertex(1551484800000L, 36L));
        chart1Points.add(new Vertex(1551571200000L, 27L));
        chart1Points.add(new Vertex(1551657600000L, 54L));
        chart1Points.add(new Vertex(1551744000000L, 89L));
        chart1Points.add(new Vertex(1551830400000L, 50L));
        chart1Points.add(new Vertex(1551916800000L, 73L));
        chart1Points.add(new Vertex(1552003200000L, 52L));

        chartState = new ChartState();

        ChartData chart0 = new ChartData("#0", "#3DC23F");
        ChartData chart1 = new ChartData("#1", "#F34C44");

        chart0.originalData.addAll(chart0Points);
        chart1.originalData.addAll(chart1Points);

        chartState.charts.add(chart0);
        chartState.charts.add(chart1);

        fillAxisPoints(chart0);
//        fillAxisPoints(chart1);
    }

    private void fillAxisPoints(ChartData chartData) {
        final long x0 = chartData.originalData.get(0).x;
        final float xLast = chartData.originalData.get(chartData.originalData.size() - 1).x;

//        final int count = chartData.originalData.size();
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

//        for (final Vertex point : chartData.originalData) {
//            chartState.xAxis.add(
//                    new AxisVertex(
//                            point.x,
//                            0,
//                            DateUtils.humanizeDate(new Date(point.x)).fallThroughAxisX(",")[0],
//                            null)
//            );
//        }

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

        for (final Vertex vertex : chartData.originalData) {
            chartData.minimapPoints.add(projectVertex(vertex, rect, x0, y0, xLast, yMax));
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
        final float deltaTime = (time - lastTime) / 1000000f / 10000f;

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
                    chartState.axisOpacityChangeSpeed
            );
        }

        for (final AxisVertex vertex : chartState.yAxis) {
            vertex.opacity = MathUtils.interpTo(
                    vertex.opacity,
                    vertex.stateOpacity,
                    deltaTime,
                    chartState.axisOpacityChangeSpeed
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
                chartState.yAxisCurrent.add(
                        new AxisVertex(
                                0,
                                0,
                                "-",
                                0f,
                                0f,
                                null,
                                0f,
                                0f
                        )
                );

                chartState.yAxisPast.add(
                        new AxisVertex(
                                0,
                                0,
                                "-",
                                0f,
                                0f,
                                null,
                                0f,
                                0f
                        )
                );
            }

            chartState.yAxis.clear();
            chartState.yAxis.addAll(chartState.yAxisCurrent);
            chartState.yAxis.addAll(chartState.yAxisPast);
        }

        if (Math.floor(chartState.lastStatePreviewMaxY) != Math.floor(chartState.statePreviewMaxY)) {
            final float deltaMaxY = ((rect.bottom - rect.top) / 5f) * ((chartState.lastStatePreviewMaxY - chartState.statePreviewMaxY) / Math.max(chartState.lastStatePreviewMaxY, chartState.statePreviewMaxY));

            for (int i = 0; i < chartState.yAxisPast.size(); ++i) {
                AxisVertex vertex = chartState.yAxisPast.get(i);
                AxisVertex current = chartState.yAxisCurrent.get(i);

                vertex.x = current.x;
                vertex.y = current.y;
                vertex.title = current.title;

                if (vertex.opacity < 0.01f) {
                    vertex.yOffset = 0f;
                    vertex.stateYOffset = -deltaMaxY;
                } else {
                    vertex.stateYOffset -= deltaMaxY;
                }

                vertex.stateOpacity = 0f;
                vertex.opacity = 1f;
            }

            final long delta = (int) ((rect.bottom - rect.top - chartState.axisYTopPadding) / 5f);
            final long deltaValue = (long) (chartState.statePreviewMaxY / 5f);

            long current = rect.bottom;
            long currentValue = 0;

            for (AxisVertex vertex : chartState.yAxisCurrent) {
                current -= delta;
                currentValue += deltaValue;

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
