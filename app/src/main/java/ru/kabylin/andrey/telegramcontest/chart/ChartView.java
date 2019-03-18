package ru.kabylin.andrey.telegramcontest.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

import java.util.List;

public final class ChartView extends View {
    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final ChartSolver chartSolver = new ChartSolverImpl();
    final ChartUserInteractor userInteractor = new ChartUserInteractorImpl(chartSolver);

    private Rect minimapRect = new Rect();
    private Rect previewRect = new Rect();
    private Rect minimapOverlayLeftRect = new Rect();
    private Rect minimapOverlayRightRect = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        drawMinimap(canvas);
        drawMinimapPreview(canvas);

        drawPreview(canvas);
        drawAxisX(canvas);

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

        chartSolver.calculateMinimapPoints(minimapRect);
        final ChartState state = chartSolver.getState();

        paint.setStrokeWidth(1);

        for (final ChartData chart : state.charts) {
            paint.setColor(Color.parseColor(chart.color));
            paint.setAlpha((int) (chart.opacity * 255f));
            drawPath(canvas, chart.minimapPoints);
        }
    }

    private void drawMinimapPreview(Canvas canvas) {
        final ChartState state = chartSolver.getState();

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);

        final Rect previewRect = state.getMinimapPreviewRect();

        canvas.drawLine(previewRect.left, previewRect.top + 1, previewRect.right, previewRect.top + 1, paint);
        canvas.drawLine(previewRect.left, previewRect.bottom - 1, previewRect.right, previewRect.bottom - 1, paint);

        canvas.drawLine(previewRect.left, previewRect.top, previewRect.left, previewRect.bottom, paint);
        canvas.drawLine(previewRect.right, previewRect.top, previewRect.right, previewRect.bottom, paint);

        paint.setColor(Color.argb(50, 0, 0, 0));

        minimapOverlayLeftRect.set(minimapRect.left, minimapRect.top, previewRect.left, minimapRect.bottom);
        minimapOverlayRightRect.set(previewRect.right, minimapRect.top, minimapRect.right, minimapRect.bottom);

        canvas.drawRect(minimapOverlayLeftRect, paint);
        canvas.drawRect(minimapOverlayRightRect, paint);

        // Draw minimap area size for debug
        paint.setColor(Color.RED);

        // Left
        canvas.drawLine(previewRect.left, previewRect.top + 1, previewRect.left + state.minimapPreviewResizeAreaSize, previewRect.top + 1, paint);
        canvas.drawLine(previewRect.left, previewRect.bottom - 1, previewRect.left + state.minimapPreviewResizeAreaSize, previewRect.bottom - 1, paint);

        canvas.drawLine(previewRect.left, previewRect.top, previewRect.left, previewRect.bottom, paint);
        canvas.drawLine(previewRect.left + state.minimapPreviewResizeAreaSize, previewRect.top, previewRect.left + state.minimapPreviewResizeAreaSize, previewRect.bottom, paint);

        // Right
        canvas.drawLine(previewRect.right - state.minimapPreviewResizeAreaSize, previewRect.top + 1, previewRect.right, previewRect.top + 1, paint);
        canvas.drawLine(previewRect.right - state.minimapPreviewResizeAreaSize, previewRect.bottom - 1, previewRect.right, previewRect.bottom - 1, paint);

        canvas.drawLine(previewRect.right - state.minimapPreviewResizeAreaSize, previewRect.top, previewRect.right - state.minimapPreviewResizeAreaSize, previewRect.bottom, paint);
        canvas.drawLine(previewRect.right, previewRect.top, previewRect.right, previewRect.bottom, paint);
    }

    private void drawPreview(Canvas canvas) {
        previewRect.set(
                /* left */ 0,
                /* top*/ 0,
                /* right */ getWidth(),
                /* bottom */ getHeight() - (int) MeasureUtils.convertDpToPixel(50)
        );

        chartSolver.calculatePreviewPoints(previewRect);
        final ChartState state = chartSolver.getState();

        paint.setStrokeWidth(3);

        for (final ChartData chart : state.charts) {
            paint.setColor(Color.parseColor(chart.color));
            paint.setAlpha((int) (chart.opacity * 255f));
            drawPath(canvas, chart.previewPoints);
        }
    }

    private void drawPath(Canvas canvas, List<Vertex> points) {
        Vertex prevVertex = null;

        for (final Vertex vertex : points) {
            if (prevVertex != null) {
                canvas.drawLine(prevVertex.x, prevVertex.y, vertex.x, vertex.y, paint);
            }

            prevVertex = vertex;
        }
    }

    private void drawAxisX(Canvas canvas) {
        chartSolver.calculateAxisXPoints(previewRect);
        final ChartState state = chartSolver.getState();

        paint.setColor(Color.BLACK);
        paint.setTextSize((int) MeasureUtils.convertDpToPixel(14));

        for (final AxisVertex vertex : state.previewAxisX) {
            final int opacity = (int) (vertex.opacity * 255f);

            if (opacity > 0) {
//                paint.setTextAlign(vertex.textAlign);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setAlpha(opacity);
                canvas.drawText(vertex.title, vertex.x, vertex.y, paint);
            }
        }
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        return userInteractor.onTouchEvent(event);
    }

    public boolean setChartVisibilityByName(final String name, final boolean visibility) {
        return chartSolver.setChartVisibilityByName(name, visibility);
    }
}
