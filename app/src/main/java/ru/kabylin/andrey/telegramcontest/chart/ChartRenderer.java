package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

import ru.kabylin.andrey.telegramcontest.helpers.MathUtils;
import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

public final class ChartRenderer implements OnPopupEventsListener {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint stackedBarsPaint = new Paint();
    private final Paint stackedAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint stackedAreaPaintMinimap = new Paint();

    final ChartSolver chartSolver = new ChartSolverImpl();

    private Rect minimapRect = new Rect();
    private Rect previewRect = new Rect();

    boolean isInit = false;
    private int width;
    private int height;

    private final ChartStyle style;
    private final Path stackedAreaPath = new Path();
    private float barsOpacity = 1f;
    private float barsOpacityState = 1f;

    @SuppressWarnings("FieldCanBeLocal")
    private float barsOpacityChangeSpeed = 100f;

    ChartRenderer(ChartStyle style) {
        this.style = style;
    }

    public void setChartState(ChartState chartState) {
        chartSolver.setChartState(chartState);
        chartState.popup.setOnPopupEventsListener(this);
        isInit = true;
    }

    void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void onDraw(Canvas canvas) {
        if (!isInit) {
            return;
        }

        drawMinimap(canvas);
        drawPreview(canvas);
        drawPopupUnderLine(canvas);
        drawIntersectPoints(canvas);
        drawAxisXLabels(canvas);
        drawAxisYLabels(canvas);
        drawAxisY2Labels(canvas);
        drawAxisYGrid(canvas);
        drawPopup(canvas);
    }

    void onProgress(float deltaTime) {
        chartSolver.onProgress(deltaTime);

        barsOpacity = MathUtils.interpTo(
                barsOpacity,
                barsOpacityState,
                deltaTime,
                barsOpacityChangeSpeed
        );
    }

    @Override
    public void onPopupDrop() {
        barsOpacityState = 0.5f;
    }

    @Override
    public void onPopupHide() {
        barsOpacityState = 1f;
    }

    private int chartsAlpha(float opacity) {
        final ChartState state = chartSolver.getState();
        return (int) (opacity * state.chartsOpacity * 255f);
    }

    private int chartsAlpha() {
        final ChartState state = chartSolver.getState();
        return (int) (state.chartsOpacity * 255f);
    }

    private void drawMinimap(Canvas canvas) {
        minimapRect.set(
                /* left */ 0,
                /* top*/ height - (int) MeasureUtils.convertDpToPixel(50),
                /* right */ width,
                /* bottom */ height
        );

        final ChartState state = chartSolver.getState();

        if (state.chartType == ChartType.LINES_2Y) {
            chartSolver.calculate2YMinimapPoints(minimapRect);
        } else {
            chartSolver.calculateMinimapPoints(minimapRect);
        }

        final int alpha = chartsAlpha();

        paint.setStrokeWidth(1);
        paint.setAlpha(alpha);

        switch (state.chartType) {
            case LINES_2Y:
            case LINES:
                drawPaths(canvas, state.charts, ChartData.SourceType.MINIMAP, state.chartsOpacity);
                break;

            case BARS:
                drawStackedBars(canvas, minimapRect, state.charts, ChartData.SourceType.MINIMAP, 1f);
                break;

            case STACKED_AREA:
                drawStackedAreas(canvas, state.charts, ChartData.SourceType.MINIMAP, stackedAreaPaintMinimap);
                break;

            default:
                drawPaths(canvas, state.charts, ChartData.SourceType.MINIMAP, state.chartsOpacity);
        }
    }

    private void drawPreview(Canvas canvas) {
        previewRect.set(
                /* left */ 0,
                /* top*/ 0,
                /* right */ width,
                /* bottom */ height - (int) MeasureUtils.convertDpToPixel(90)
        );

        final ChartState state = chartSolver.getState();

        if (state.chartType == ChartType.LINES_2Y) {
            chartSolver.calculate2YPreviewPoints(previewRect);
        } else {
            chartSolver.calculatePreviewPoints(previewRect);
        }

        paint.setStrokeWidth(3);

        switch (state.chartType) {
            case LINES_2Y:
            case LINES:
                drawPaths(canvas, state.charts, ChartData.SourceType.PREVIEW, state.chartsOpacity);
                break;

            case BARS:
                drawStackedBars(canvas, previewRect, state.charts, ChartData.SourceType.PREVIEW, barsOpacity);
                break;

            case STACKED_AREA:
                drawStackedAreas(canvas, state.charts, ChartData.SourceType.PREVIEW, stackedAreaPaint);
                break;

            default:
                drawPaths(canvas, state.charts, ChartData.SourceType.PREVIEW, state.chartsOpacity);
        }
    }

    private void drawPaths(Canvas canvas, List<ChartData> charts, ChartData.SourceType source, float opacity) {
        for (final ChartData chart : charts) {
            final int alpha = (int) (chart.opacity * opacity * 255f);

            if (alpha <= 0) {
                continue;
            }

            paint.setColor(chart.color);
            paint.setAlpha(alpha);

            switch (source) {
                case MINIMAP:
                    drawPath(canvas, chart.minimapPoints);
                    break;

                case PREVIEW:
                    drawPath(canvas, chart.previewPoints);
                    break;
            }
        }
    }

    private void drawStackedAreas(Canvas canvas, List<ChartData> charts, ChartData.SourceType source, Paint paint) {
        for (final ChartData chart : charts) {
            paint.setColor(chart.color);
            paint.setAlpha(chartsAlpha(chart.opacity));

            switch (source) {
                case MINIMAP:
                    drawStackedPath(canvas, minimapRect, chart.minimapPoints, paint);
                    break;

                case PREVIEW:
                    drawStackedPath(canvas, previewRect, chart.previewPoints, paint);
                    break;
            }
        }
    }

    private void drawPath(Canvas canvas, List<Vertex> points) {
        final float[] rawPoints = new float[points.size() * 4];

        int index = 0;

        for (int i = 0; i < points.size() - 1; ++i) {
            rawPoints[index++] = points.get(i).x;
            rawPoints[index++] = points.get(i).y;
            rawPoints[index++] = points.get(i + 1).x;
            rawPoints[index++] = points.get(i + 1).y;
        }

        canvas.drawLines(rawPoints, paint);
    }

    private void drawStackedPath(Canvas canvas, Rect rect, List<Vertex> points, Paint paint) {
        stackedAreaPath.reset();

        Vertex lastPoint = null;

        for (final Vertex point : points) {
            if (lastPoint == null) {
                stackedAreaPath.moveTo(point.x, rect.bottom);
                stackedAreaPath.lineTo(point.x, point.y);
                lastPoint = point;
            } else {
                stackedAreaPath.lineTo(point.x, point.y);
                lastPoint = point;
            }
        }

        if (lastPoint != null) {
            stackedAreaPath.lineTo(lastPoint.x, rect.bottom);
        }

        canvas.drawPath(stackedAreaPath, paint);
    }

    private void drawStackedBars(Canvas canvas, Rect previewRect, List<ChartData> charts, ChartData.SourceType source, float opacity) {
        List<StackedVertex> stack = new ArrayList<>();

        Vertex lastPoint = null;
        final List<Vertex> points;

        switch (source) {
            case MINIMAP:
                points = charts.get(0).minimapPoints;
                break;

            case PREVIEW:
                points = charts.get(0).previewPoints;
                break;

            default:
                points = charts.get(0).previewPoints;
        }

        for (int i = 0; i < points.size(); ++i) {
            final Vertex point = points.get(i);

            if (lastPoint == null) {
                lastPoint = point;
                continue;
            }

            stack.clear();

            for (final ChartData chart : charts) {
                final float value;

                switch (source) {
                    case MINIMAP:
                        value = chart.minimapPoints.get(i).y;
                        break;

                    case PREVIEW:
                        value = chart.previewPoints.get(i).y;
                        break;

                    default:
                        value = chart.previewPoints.get(i).y;
                }

                stack.add(
                        0,
                        new StackedVertex(
                                value,
                                chart.opacity,
                                chart.color
                        )
                );
            }

            drawStackedBar(canvas, previewRect, lastPoint.x, point.x, stack, opacity);
            lastPoint = point;
        }
    }

    private void drawStackedBar(Canvas canvas, Rect previewRect, float x1, float x2, List<StackedVertex> stack, float opacity) {
        float bottom = previewRect.bottom;
        final ChartState state = chartSolver.getState();

        if (state.intersectX == x1) {
            opacity = 1f;
        }

        for (StackedVertex vertex : stack) {
            stackedBarsPaint.setColor(vertex.color);
            stackedBarsPaint.setAlpha(chartsAlpha(vertex.opacity * opacity));

            if (vertex.y > bottom) {
                continue;
            }

            final RectF rect = new RectF(x1, vertex.y, x2, bottom);
            bottom = vertex.y;
            canvas.drawRect(rect, stackedBarsPaint);
        }
    }

    private void drawAxisXLabels(Canvas canvas) {
        chartSolver.calculateAxisXPoints(previewRect);
        final ChartState state = chartSolver.getState();

        paint.setColor(style.chartAxisTextColor);
        paint.setTextSize((int) MeasureUtils.convertDpToPixel(14));

        for (final AxisVertex vertex : state.previewAxisX) {
            final int alpha = chartsAlpha(vertex.opacity);

            if (alpha > 0) {
                paint.setTextAlign(vertex.original.textAlign);
                paint.setAlpha(alpha);
                canvas.drawText(vertex.title, vertex.x, vertex.y, paint);
            }
        }
    }

    private void drawAxisYLabels(Canvas canvas) {
        final ChartState state = chartSolver.getState();
        paint.setTextSize((int) MeasureUtils.convertDpToPixel(14));

        for (final AxisVertex vertex : state.previewAxisY) {
            final int opacity = (int) (vertex.opacity * state.chartsOpacity * 255f);
            final float y = vertex.y - state.axisYTextOffsetY + vertex.yOffset;

            if (opacity > 0 && y <= previewRect.bottom) {
                paint.setTextAlign(Paint.Align.LEFT);

                if (state.chartType == ChartType.LINES || state.chartType == ChartType.LINES_2Y) {
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(style.chartBackgroundColor);
                    paint.setAlpha(opacity);
                    paint.setStrokeWidth(MeasureUtils.convertDpToPixel(4));
                    canvas.drawText(vertex.title, vertex.x, y, paint);
                }

                paint.setStyle(Paint.Style.FILL);

                if (state.chartType == ChartType.LINES_2Y) {
                    paint.setColor(state.charts.get(0).color);
                } else {
                    paint.setColor(style.chartAxisTextColor);
                }

                paint.setAlpha(opacity);
                canvas.drawText(vertex.title, vertex.x, y, paint);
            }
        }
    }

    private void drawAxisY2Labels(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        if (state.chartType != ChartType.LINES_2Y) {
            return;
        }

        paint.setTextSize((int) MeasureUtils.convertDpToPixel(14));

        for (final AxisVertex vertex : state.previewAxisY2) {
            final int alpha = chartsAlpha(vertex.opacity);
            final float y = vertex.y - state.axisYTextOffsetY + vertex.yOffset;

            if (alpha > 0 && y <= previewRect.bottom) {
                paint.setTextAlign(Paint.Align.RIGHT);

                if (state.chartType == ChartType.LINES || state.chartType == ChartType.LINES_2Y) {
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(style.chartBackgroundColor);
                    paint.setAlpha(alpha);
                    paint.setStrokeWidth(MeasureUtils.convertDpToPixel(4));
                    canvas.drawText(vertex.title, vertex.x, y, paint);
                }

                paint.setStyle(Paint.Style.FILL);

                if (state.chartType == ChartType.LINES_2Y) {
                    paint.setColor(state.charts.get(1).color);
                } else {
                    paint.setColor(style.chartAxisTextColor);
                }

                paint.setAlpha(alpha);
                canvas.drawText(vertex.title, vertex.x, y, paint);
            }
        }
    }

    private void drawAxisYGrid(Canvas canvas) {
        chartSolver.calculateAxisYPoints(previewRect);
        final ChartState state = chartSolver.getState();

        paint.setStrokeWidth(MeasureUtils.convertDpToPixel(1));
        paint.setColor(style.chartGridColor);

        for (final AxisVertex vertex : state.previewAxisY) {
            final int alpha = chartsAlpha(vertex.opacity * 0.1f);
            final float y;

            if (state.chartType == ChartType.LINES_2Y) {
                y = vertex.y;
            } else {
                y = vertex.y + vertex.yOffset;
            }

            if (alpha > 0 && y <= previewRect.bottom) {
                paint.setAlpha(alpha);
                canvas.drawLine(previewRect.left, y, previewRect.right, y, paint);
            }
        }
    }

    private void drawPopupUnderLine(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        if (state.chartType == ChartType.BARS) {
            return;
        }

        if (!state.popup.isVisible) {
            return;
        }

        paint.setStrokeWidth(MeasureUtils.convertDpToPixel(1));
        paint.setColor(style.chartPopupLineColor);

        canvas.drawLine(state.popup.left, previewRect.top, state.popup.left, previewRect.bottom, paint);
    }

    private void drawIntersectPoints(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        if (state.chartType == ChartType.BARS) {
            return;
        }

        if (!state.popup.isVisible) {
            return;
        }

        for (int i = 0; i < state.charts.size(); ++i) {
            final ChartData chart = state.charts.get(i);
            final Vertex vertex = state.popupIntersectPoints.get(i);

            if (chart.isVisible) {
                paint.setColor(style.chartBackgroundColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(vertex.x, vertex.y, state.intersectPointSize, paint);

                paint.setColor(chart.color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(state.intersectPointStrokeWidth);
                canvas.drawCircle(vertex.x, vertex.y, state.intersectPointSize, paint);
            }
        }

        paint.setStyle(Paint.Style.FILL);
    }

    private void drawPopup(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        state.popup.chartColorPopupColor = style.chartColorPopupColor;
        state.popup.chartColorPopupTitleColor = style.chartColorPopupTitleColor;
        state.popup.draw(canvas, state.previewRect);
    }
}
