package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Rect;
import android.support.v4.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class TestChartService implements ChartService {
    private final ChartState chartState;

    TestChartService() {
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
    }

    @Override
    public void calculateMinimapPoints(final Rect rect) {
        chartState.minimapRect = rect;
        float yMax = findMaxByYInCharts(chartState.charts);

        for (final ChartData chartData : chartState.charts) {
            calculateChartMinimapPoints(rect, yMax, chartData);
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
        float yMax = findMaxByYInCharts(chartState.charts);

        for (final ChartData chartData : chartState.charts) {
            calculatePreviewPoints(rect, yMax, chartData);
        }
    }

    private void calculatePreviewPoints(final Rect rect, final float yMax, final ChartData chartData) {
        chartData.previewPoints.clear();
        final List<Vertex> points = getMinimapPreviewPoints(chartData);

        final long x0 = chartState.minimapPreviewPosition;
        final long y0 = chartState.minimapRect.bottom;

        final float xLast = chartState.minimapPreviewPosition + chartState.minimapPreviewSize;

        for (final Vertex vertex : points) {
            chartData.previewPoints.add(projectVertex(vertex, rect, x0, y0, xLast, yMax));
        }
    }

    private Vertex projectVertex(Vertex vertex, Rect rect, float x0, float y0, float xMax, float yMax) {
        float x = rect.left + (vertex.x - x0) / (xMax - x0) * (rect.right - rect.left);
        float y = rect.bottom - (vertex.y / yMax) * (rect.bottom - rect.top);

        return new Vertex(x, y);
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

            if (point.x >= chartState.minimapPreviewPosition && point.x <= chartState.minimapPreviewPosition + chartState.minimapPreviewSize) {
                // Add point before left border
                if (prevPoint != null && !firstPointSet) {
                    previewOriginalPoints.add(prevPoint);
                }

                previewOriginalPoints.add(point);
                firstPointSet = true;
            }
            else if (firstPointSet) {
                // Add point after right border
                previewOriginalPoints.add(point);
                break;
            }
            else {
                prevPoint = point;
            }
        }

        return previewOriginalPoints;
    }

    private float findMaxByYInCharts(final List<ChartData> charts) {
        float yMax = 0;

        for (final ChartData chartData : charts) {
            final float max = findMaxByY(chartData.originalData).y;

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
    public void setMinimapPoisiton(float newPosition) {
        final Rect minimapRect = chartState.minimapRect;
        chartState.minimapPreviewPosition = MathUtils.clamp(
                (int) newPosition,
                minimapRect.left,
                minimapRect.right - chartState.minimapPreviewSize
        );
    }
}
