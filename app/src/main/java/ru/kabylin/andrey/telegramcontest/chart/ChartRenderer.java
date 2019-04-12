package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

public final class ChartRenderer {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint stackedBarsPaint = new Paint();
    private final Paint stackedAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint stackedAreaPaintMinimap = new Paint();

    final ChartSolver chartSolver = new ChartSolverImpl();

    private Rect minimapRect = new Rect();
    private Rect previewRect = new Rect();
    private Rect minimapOverlayLeftRect = new Rect();
    private Rect minimapOverlayRightRect = new Rect();
    private Rect minimapBorderLeftRect = new Rect();
    private Rect minimapBorderRightRect = new Rect();
    private Rect minimapBorderTopRect = new Rect();
    private Rect minimapBorderBottomRect = new Rect();

    private boolean isInit = false;
    private int width;
    private int height;

    private final ChartStyle style;
    private final Path stackedAreaPath = new Path();

    ChartRenderer(ChartStyle style) {
        this.style = style;
    }

    public void setChartState(ChartState chartState) {
        chartSolver.setChartState(chartState);
        isInit = true;
    }

    boolean onDraw(Canvas canvas, int width, int height) {
        this.width = width;
        this.height = height;

        if (!isInit) {
            return false;
        }

        if (chartsAlpha() == 0) {
            chartSolver.onProgress();
            return false;
        }

        boolean isRender;

        isRender = drawMinimap(canvas);
        isRender = isRender | drawMinimapPreview(canvas);
        isRender = isRender | drawMinimapPreview(canvas);
        isRender = isRender | drawPopupUnderLine(canvas);
        isRender = isRender | drawPreview(canvas);
        isRender = isRender | drawIntersectPoints(canvas);
        isRender = isRender | drawAxisXLabels(canvas);
        isRender = isRender | drawAxisYLabels(canvas);
        isRender = isRender | drawAxisY2Labels(canvas);
        isRender = isRender | drawAxisYGrid(canvas);
        isRender = isRender | drawPopup(canvas);

        chartSolver.onProgress();
        return isRender;
    }

    private int chartsAlpha(float opacity) {
        final ChartState state = chartSolver.getState();
        return (int) (opacity * state.chartsOpacity * 255f);
    }

    private int chartsAlpha() {
        final ChartState state = chartSolver.getState();
        return (int) (state.chartsOpacity * 255f);
    }

    boolean onDrawCharts(Canvas canvas, int width, int height) {
        this.width = width;
        this.height = height;

        if (!isInit) {
            return false;
        }

        drawPreview(canvas);
        chartSolver.onProgress();
        return true;
    }

    private boolean drawMinimap(Canvas canvas) {
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
                drawStackedBars(canvas, minimapRect, state.charts, ChartData.SourceType.MINIMAP);
                break;

            case STACKED_AREA:
                drawStackedAreas(canvas, state.charts, ChartData.SourceType.MINIMAP, stackedAreaPaintMinimap);
                break;

            default:
                drawPaths(canvas, state.charts, ChartData.SourceType.MINIMAP, state.chartsOpacity);
        }

        return alpha != 0;
    }

    private boolean drawMinimapPreview(Canvas canvas) {
        final ChartState state = chartSolver.getState();
        final Rect previewRect = state.getMinimapPreviewRect();

        // Overlay
        paint.setColor(style.chartMinimapOverlayColor);

        minimapOverlayLeftRect.set(minimapRect.left, minimapRect.top, previewRect.left, minimapRect.bottom);
        minimapOverlayRightRect.set(previewRect.right, minimapRect.top, minimapRect.right, minimapRect.bottom);

        canvas.drawRect(minimapOverlayLeftRect, paint);
        canvas.drawRect(minimapOverlayRightRect, paint);

        // Border
        paint.setColor(style.chartMinimapBorderColor);
//        final int alpha = chartsAlpha();
//        paint.setAlpha(alpha);

        minimapBorderLeftRect.set(
                previewRect.left,
                previewRect.top,
                previewRect.left + state.minimapPreviewRenderResizeAreaSize,
                previewRect.bottom
        );

        minimapBorderRightRect.set(
                previewRect.right - state.minimapPreviewRenderResizeAreaSize,
                previewRect.top,
                previewRect.right,
                previewRect.bottom
        );

        minimapBorderTopRect.set(
                previewRect.left + state.minimapPreviewRenderResizeAreaSize,
                previewRect.top,
                previewRect.right - state.minimapPreviewRenderResizeAreaSize,
                previewRect.top + state.minimapPreviewBorderHeight
        );

        minimapBorderBottomRect.set(
                previewRect.left + state.minimapPreviewRenderResizeAreaSize,
                previewRect.bottom - state.minimapPreviewBorderHeight,
                previewRect.right - state.minimapPreviewRenderResizeAreaSize,
                previewRect.bottom
        );

        canvas.drawRect(minimapBorderLeftRect, paint);
        canvas.drawRect(minimapBorderRightRect, paint);
        canvas.drawRect(minimapBorderTopRect, paint);
        canvas.drawRect(minimapBorderBottomRect, paint);

        return true;
    }

    private boolean drawPreview(Canvas canvas) {
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
                drawStackedBars(canvas, previewRect, state.charts, ChartData.SourceType.PREVIEW);
                break;

            case STACKED_AREA:
                drawStackedAreas(canvas, state.charts, ChartData.SourceType.PREVIEW, stackedAreaPaint);
                break;

            default:
                drawPaths(canvas, state.charts, ChartData.SourceType.PREVIEW, state.chartsOpacity);
        }

        return chartsAlpha() != 0;
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
            paint.setAlpha((int) (chart.opacity * 255f));

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

    private void drawStackedBars(Canvas canvas, Rect previewRect, List<ChartData> charts, ChartData.SourceType source) {
        List<StackedVertex> stack = new ArrayList<>();
        List<StackedVertex> lastStack = new ArrayList<>();

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
                        value = (float) chart.minimapPoints.get(i).y;
                        break;

                    case PREVIEW:
                        value = (float) chart.previewPoints.get(i).y;
                        break;

                    default:
                        value = (float) chart.previewPoints.get(i).y;
                }

                stack.add(
                        new StackedVertex(
                                value,
                                chart.opacity,
                                chart.color
                        )
                );
            }

            Collections.reverse(stack);
            drawStackedBar(canvas, previewRect, lastPoint.x, point.x, stack);
            lastPoint = point;
        }
    }

    private void drawStackedBar(Canvas canvas, Rect previewRect, float x1, float x2, List<StackedVertex> stack) {
        float bottom = previewRect.bottom;

        for (StackedVertex vertex : stack) {
            stackedBarsPaint.setColor(vertex.color);
            stackedBarsPaint.setAlpha((int) (vertex.opacity * 255f));

            if (vertex.y > bottom) {
                continue;
            }

            final RectF rect = new RectF(x1, vertex.y, x2, bottom);
            bottom = vertex.y;
            canvas.drawRect(rect, stackedBarsPaint);
        }
    }

    private boolean drawAxisXLabels(Canvas canvas) {
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

        return chartsAlpha() != 0;
    }

    private boolean drawAxisYLabels(Canvas canvas) {
        final ChartState state = chartSolver.getState();
        paint.setTextSize((int) MeasureUtils.convertDpToPixel(14));

        for (final AxisVertex vertex : state.previewAxisY) {
            final int opacity = (int) (vertex.opacity * state.chartsOpacity * 255f);
            final float y = vertex.y - state.axisYTextOffsetY + vertex.yOffset;

            if (opacity > 0 && y <= previewRect.bottom) {
                paint.setTextAlign(Paint.Align.LEFT);

                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(style.chartBackgroundColor);
                paint.setAlpha(opacity);
                paint.setStrokeWidth(MeasureUtils.convertDpToPixel(4));
                canvas.drawText(vertex.title, vertex.x, y, paint);

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

        return chartsAlpha() != 0;
    }

    private boolean drawAxisY2Labels(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        if (state.chartType != ChartType.LINES_2Y) {
            return false;
        }

        paint.setTextSize((int) MeasureUtils.convertDpToPixel(14));

        for (final AxisVertex vertex : state.previewAxisY2) {
            final int alpha = chartsAlpha(vertex.opacity);
            final float y = vertex.y - state.axisYTextOffsetY + vertex.yOffset;

            if (alpha > 0 && y <= previewRect.bottom) {
                paint.setTextAlign(Paint.Align.RIGHT);

                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(style.chartBackgroundColor);
                paint.setAlpha(alpha);
                paint.setStrokeWidth(MeasureUtils.convertDpToPixel(4));
                canvas.drawText(vertex.title, vertex.x, y, paint);

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

        return chartsAlpha() != 0;
    }

    private boolean drawAxisYGrid(Canvas canvas) {
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

        return chartsAlpha() != 0;
    }

    private boolean drawPopupUnderLine(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        if (!state.popup.isVisible) {
            return false;
        }

        paint.setStrokeWidth(MeasureUtils.convertDpToPixel(1));
        paint.setColor(style.chartPopupLineColor);

        canvas.drawLine(state.popup.left, previewRect.top, state.popup.left, previewRect.bottom, paint);
        return true;
    }

    private boolean drawIntersectPoints(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        if (!state.popup.isVisible) {
            return false;
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
        return true;
    }

    private boolean drawPopup(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        state.popup.chartColorPopupColor = style.chartColorPopupColor;
        state.popup.chartColorPopupTitleColor = style.chartColorPopupTitleColor;
        state.popup.draw(canvas, state.previewRect);

        return true;
    }
}
