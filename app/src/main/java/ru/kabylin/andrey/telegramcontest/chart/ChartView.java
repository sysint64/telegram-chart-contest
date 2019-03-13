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
import android.util.Log;
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
    final ChartService chartService = new TestChartService();

    private Rect minimapRect = new Rect();
    private Rect previewRect = new Rect();
    private Rect minimapOverlayLeftRect = new Rect();
    private Rect minimapOverlayRightRect = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        drawMinimap(canvas);
        drawMinimapPreview(canvas);

        drawPreview(canvas);

        invalidate();
    }

    private void drawMinimap(Canvas canvas) {
        minimapRect.set(
                /* left */ 0,
                /* top*/ getHeight() - (int) MeasureUtils.convertDpToPixel(50),
                /* right */ getWidth(),
                /* bottom */ getHeight()
        );

        chartService.calculateMinimapPoints(minimapRect);
        final ChartState state = chartService.getState();

        paint.setStrokeWidth(1);

        for (final ChartData chart : state.charts) {
            paint.setColor(Color.parseColor(chart.color));
            drawPath(canvas, chart.minimapPoints);
        }
    }

    private void drawMinimapPreview(Canvas canvas) {
        final ChartState state = chartService.getState();

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
    }

    private void drawPreview(Canvas canvas) {
        previewRect.set(
                /* left */ 0,
                /* top*/ 0,
                /* right */ getWidth(),
                /* bottom */ getHeight() - (int) MeasureUtils.convertDpToPixel(50)
        );

        chartService.calculatePreviewPoints(previewRect);
        final ChartState state = chartService.getState();

        paint.setStrokeWidth(2);

        for (final ChartData chart : state.charts) {
            paint.setColor(Color.parseColor(chart.color));
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

    private State state = State.NONE;

    enum State {
        NONE,
        MINIMAP_MOVE
    }

    private float lastTouchX = 0;
    private float lastTouchY = 0;

    private float onActionDownTouchX = 0;
    private float onActionDownTouchY = 0;

    private float onActionDownMinimapPreviewPosition = 0;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        final float touchX = event.getX();
        final float touchY = event.getY();

        final ChartState chartState = chartService.getState();
        final Rect previewRect = chartState.getMinimapPreviewRect();

        boolean result = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onActionDownTouchX = touchX;
                onActionDownTouchY = touchY;

                if (touchX > previewRect.left && touchX < previewRect.right &&
                        touchY > previewRect.top && touchY < previewRect.bottom)
                {
                    state = State.MINIMAP_MOVE;
                    onActionDownMinimapPreviewPosition = chartState.minimapPreviewPosition;
                    Log.d("ChartView", "update state to MINIMAP_MOVE");
                    result = true;
                    break;
                }

            case MotionEvent.ACTION_UP:
                state = State.NONE;
                Log.d("ChartView", "update state to NONE");
                break;

            case MotionEvent.ACTION_MOVE:
                chartService.setMinimapPoisiton(onActionDownMinimapPreviewPosition + touchX - onActionDownTouchX);
                break;
        }

        lastTouchX = touchX;
        lastTouchY = touchY;

        return result;
    }
}
