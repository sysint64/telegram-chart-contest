package ru.kabylin.andrey.telegramcontest.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import ru.kabylin.andrey.telegramcontest.ChartViewLayoutManager;
import ru.kabylin.andrey.telegramcontest.R;
import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ChartView extends View {

    enum ChartType {
        LINES,
        LINES_2Y,
        BARS,
        STACKED_AREA,
        PIE
    }

    final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Paint stackedBarsPaint = new Paint();
    final Paint stackedAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Paint stackedAreaPaintMinimap = new Paint();

    final ChartSolver chartSolver = new ChartSolverImpl();
    final ChartUserInteractor userInteractor = new ChartUserInteractorImpl(chartSolver);

    private Rect minimapRect = new Rect();
    private Rect previewRect = new Rect();
    private Rect minimapOverlayLeftRect = new Rect();
    private Rect minimapOverlayRightRect = new Rect();
    private Rect minimapBorderLeftRect = new Rect();
    private Rect minimapBorderRightRect = new Rect();
    private Rect minimapBorderTopRect = new Rect();
    private Rect minimapBorderBottomRect = new Rect();

    private boolean isInit = false;

    // Style
    private int chartAxisTextColor = Color.BLACK;
    private int chartGridColor = Color.BLACK;
    private int chartColorPopupColor = Color.BLACK;
    private int chartColorPopupTitleColor = Color.BLACK;
    private int chartPopupLineColor = Color.BLACK;
    private int chartMinimapOverlayColor = Color.BLACK;
    private int chartMinimapBorderColor = Color.BLACK;
    private int chartBackgroundColor = Color.BLACK;

    private ChartType chartType = ChartType.BARS;
    private ChartType zoomInChartType = ChartType.LINES;

    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChartView);
        extractStyle(a);
        a.recycle();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChartView, defStyleAttr, 0);
        extractStyle(a);
        a.recycle();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChartView, defStyleAttr, 0);
        extractStyle(a);
        a.recycle();
    }

    private void extractStyle(TypedArray a) {
        @ColorRes final int chartAxisTextColorRes = a.getResourceId(R.styleable.ChartView_chartAxisTextColor, R.color.lightThemeChartAxisText);
        chartAxisTextColor = getResources().getColor(chartAxisTextColorRes);

        @ColorRes final int chartGridColorRes = a.getResourceId(R.styleable.ChartView_chartGridColor, R.color.lightThemeChartGrid);
        chartGridColor = getResources().getColor(chartGridColorRes);

        @ColorRes final int chartPopupColorRes = a.getResourceId(R.styleable.ChartView_chartPopupColor, R.color.lightThemeChartPopup);
        chartColorPopupColor = getResources().getColor(chartPopupColorRes);

        @ColorRes final int chartPopupColorTitleRes = a.getResourceId(R.styleable.ChartView_chartPopupTitleColor, R.color.lightThemeChartPopupTitle);
        chartColorPopupTitleColor = getResources().getColor(chartPopupColorTitleRes);

        @ColorRes final int chartPopupLineColorRes = a.getResourceId(R.styleable.ChartView_chartPopupLineColor, R.color.lightThemeChartPopupLine);
        chartPopupLineColor = getResources().getColor(chartPopupLineColorRes);

        @ColorRes final int chartMinimapOverlayColorRes = a.getResourceId(R.styleable.ChartView_chartMinimapOverlayColor, R.color.lightThemeChartMinimapOverlay);
        chartMinimapOverlayColor = getResources().getColor(chartMinimapOverlayColorRes);

        @ColorRes final int chartMinimapBorderColorRes = a.getResourceId(R.styleable.ChartView_chartMinimapBorderColor, R.color.lightThemeChartMinimapBorder);
        chartMinimapBorderColor = getResources().getColor(chartMinimapBorderColorRes);

        @ColorRes final int chartBackgroundColorRes = a.getResourceId(R.styleable.ChartView_chartBackgroundColor, R.color.lightThemeChartBackground);
        chartBackgroundColor = getResources().getColor(chartBackgroundColorRes);
    }

    public void setChartState(ChartState chartState) {
        chartSolver.setChartState(chartState);
        isInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            return;
        }

        drawMinimap(canvas);
        drawMinimapPreview(canvas);
        drawPopupUnderLine(canvas);
        drawPreview(canvas);
        drawIntersectPoints(canvas);
        drawAxisXLabels(canvas);
        drawAxisYLabels(canvas);
        drawAxisY2Labels(canvas);
        drawAxisYGrid(canvas);
        drawPopup(canvas);

        chartSolver.onProgress();
        invalidate();
    }

    private void drawMinimap(Canvas canvas) {
        minimapRect.set(
                /* left */ 0,
                /* top*/ getHeight() - (int) MeasureUtils.convertDpToPixel(50),
                /* right */ getWidth(),
                /* bottom */ getHeight()
        );

        if (chartType == ChartType.LINES_2Y) {
            chartSolver.calculate2YMinimapPoints(minimapRect);
        } else {
            chartSolver.calculateMinimapPoints(minimapRect);
        }

        final ChartState state = chartSolver.getState();

        paint.setStrokeWidth(1);

        switch (chartType) {
            case LINES_2Y:
            case LINES:
                drawPaths(canvas, state.charts, ChartData.SourceType.MINIMAP);
                break;

            case BARS:
                drawStackedBars(canvas, minimapRect, state.charts, ChartData.SourceType.MINIMAP);
                break;

            case STACKED_AREA:
                drawStackedAreas(canvas, state.charts, ChartData.SourceType.MINIMAP, stackedAreaPaintMinimap);
                break;

            default:
                drawPaths(canvas, state.charts, ChartData.SourceType.MINIMAP);
        }
    }

    private void drawMinimapPreview(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        final Rect previewRect = state.getMinimapPreviewRect();

        // Overlay
        paint.setColor(chartMinimapOverlayColor);

        minimapOverlayLeftRect.set(minimapRect.left, minimapRect.top, previewRect.left, minimapRect.bottom);
        minimapOverlayRightRect.set(previewRect.right, minimapRect.top, minimapRect.right, minimapRect.bottom);

        canvas.drawRect(minimapOverlayLeftRect, paint);
        canvas.drawRect(minimapOverlayRightRect, paint);

        // Border
        paint.setColor(chartMinimapBorderColor);

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
    }

    final class StackedVertex implements Comparable<StackedVertex> {
        final float y;
        final float opacity;
        final int color;

        StackedVertex(float y, float opacity, int color) {
            this.y = y;
            this.opacity = opacity;
            this.color = color;
        }

        @Override
        public int compareTo(StackedVertex o) {
            return (int) (o.y - this.y);
        }
    }

    private void drawPreview(Canvas canvas) {
        previewRect.set(
                /* left */ 0,
                /* top*/ 0,
                /* right */ getWidth(),
                /* bottom */ getHeight() - (int) MeasureUtils.convertDpToPixel(90)
        );

        if (chartType == ChartType.LINES_2Y) {
            chartSolver.calculate2YPreviewPoints(previewRect);
        } else {
            chartSolver.calculatePreviewPoints(previewRect);
        }

        final ChartState state = chartSolver.getState();

        paint.setStrokeWidth(3);

        switch (chartType) {
            case LINES_2Y:
            case LINES:
                drawPaths(canvas, state.charts, ChartData.SourceType.PREVIEW);
                break;

            case BARS:
                drawStackedBars(canvas, previewRect, state.charts, ChartData.SourceType.PREVIEW);
                break;

            case STACKED_AREA:
                drawStackedAreas(canvas, state.charts, ChartData.SourceType.PREVIEW, stackedAreaPaint);
                break;

            default:
                drawPaths(canvas, state.charts, ChartData.SourceType.PREVIEW);
        }
    }

    private void drawPaths(Canvas canvas, List<ChartData> charts, ChartData.SourceType source) {
        for (final ChartData chart : charts) {
            paint.setColor(chart.color);
            paint.setAlpha((int) (chart.opacity * 255f));

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

    final Path stackedAreaPath = new Path();

    private void drawAxisXLabels(Canvas canvas) {
        chartSolver.calculateAxisXPoints(previewRect);
        final ChartState state = chartSolver.getState();

        paint.setColor(chartAxisTextColor);
        paint.setTextSize((int) MeasureUtils.convertDpToPixel(14));

        for (final AxisVertex vertex : state.previewAxisX) {
            final int opacity = (int) (vertex.opacity * 255f);

            if (opacity > 0) {
                paint.setTextAlign(vertex.original.textAlign);
                paint.setAlpha(opacity);
                canvas.drawText(vertex.title, vertex.x, vertex.y, paint);
            }
        }
    }

    private void drawAxisYLabels(Canvas canvas) {
        final ChartState state = chartSolver.getState();
        paint.setTextSize((int) MeasureUtils.convertDpToPixel(14));

        for (final AxisVertex vertex : state.previewAxisY) {
            final int opacity = (int) (vertex.opacity * 255f);
            final float y = vertex.y - state.axisYTextOffsetY + vertex.yOffset;

            if (opacity > 0 && y <= previewRect.bottom) {
                paint.setTextAlign(Paint.Align.LEFT);

                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(chartBackgroundColor);
                paint.setAlpha(opacity);
                paint.setStrokeWidth(MeasureUtils.convertDpToPixel(4));
                canvas.drawText(vertex.title, vertex.x, y, paint);

                paint.setStyle(Paint.Style.FILL);

                if (chartType == ChartType.LINES_2Y) {
                    paint.setColor(state.charts.get(0).color);
                } else {
                    paint.setColor(chartAxisTextColor);
                }

                paint.setAlpha(opacity);
                canvas.drawText(vertex.title, vertex.x, y, paint);
            }
        }
    }

    private void drawAxisY2Labels(Canvas canvas) {
        if (chartType != ChartType.LINES_2Y)
            return;

        final ChartState state = chartSolver.getState();
        paint.setTextSize((int) MeasureUtils.convertDpToPixel(14));

        for (final AxisVertex vertex : state.previewAxisY2) {
            final int opacity = (int) (vertex.opacity * 255f);
            final float y = vertex.y - state.axisYTextOffsetY + vertex.yOffset;

            if (opacity > 0 && y <= previewRect.bottom) {
                paint.setTextAlign(Paint.Align.RIGHT);

                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(chartBackgroundColor);
                paint.setAlpha(opacity);
                paint.setStrokeWidth(MeasureUtils.convertDpToPixel(4));
                canvas.drawText(vertex.title, vertex.x, y, paint);

                paint.setStyle(Paint.Style.FILL);

                if (chartType == ChartType.LINES_2Y) {
                    paint.setColor(state.charts.get(1).color);
                } else {
                    paint.setColor(chartAxisTextColor);
                }

                paint.setAlpha(opacity);
                canvas.drawText(vertex.title, vertex.x, y, paint);
            }
        }
    }

    private void drawAxisYGrid(Canvas canvas) {
        chartSolver.calculateAxisYPoints(previewRect);
        final ChartState state = chartSolver.getState();

        paint.setStrokeWidth(MeasureUtils.convertDpToPixel(1));
        paint.setColor(chartGridColor);

        for (final AxisVertex vertex : state.previewAxisY) {
            final int opacity = (int) (vertex.opacity * 20f);
            final float y;

            if (chartType == ChartType.LINES_2Y) {
                y = vertex.y;
            } else {
                y = vertex.y + vertex.yOffset;
            }

            if (opacity > 0 && y <= previewRect.bottom) {
                paint.setAlpha(opacity);
                canvas.drawLine(previewRect.left, y, previewRect.right, y, paint);
            }
        }
    }

    private void drawPopupUnderLine(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        if (!state.popup.isVisible) {
            return;
        }

        paint.setStrokeWidth(MeasureUtils.convertDpToPixel(1));
        paint.setColor(chartPopupLineColor);

        canvas.drawLine(state.popup.left, previewRect.top, state.popup.left, previewRect.bottom, paint);
    }

    private void drawIntersectPoints(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        if (!state.popup.isVisible) {
            return;
        }

        for (int i = 0; i < state.charts.size(); ++i) {
            final ChartData chart = state.charts.get(i);
            final Vertex vertex = state.popupIntersectPoints.get(i);

            if (chart.isVisible) {
                paint.setColor(chartBackgroundColor);
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

        state.popup.chartColorPopupColor = chartColorPopupColor;
        state.popup.chartColorPopupTitleColor = chartColorPopupTitleColor;
        state.popup.draw(canvas, state.previewRect);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        return userInteractor.onTouchEvent(event);
    }

    public boolean setChartVisibilityByName(final String name, final boolean visibility) {
        return chartSolver.setChartVisibilityByName(name, visibility);
    }

    public void setLayoutManager(ChartViewLayoutManager layoutManager) {
        userInteractor.setChartViewLayoutManager(layoutManager);
    }
}
