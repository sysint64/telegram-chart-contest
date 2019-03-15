package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

class ChartUserInteractorImpl implements ChartUserInteractor  {
    enum State {
        NONE,
        MINIMAP_MOVE,
        MINIMAP_RESIZE_LEFT,
        MINIMAP_RESIZE_RIGHT,
    }

    private final ChartSolver chartSolver;
    private State state = State.NONE;

    private float onActionDownTouchX = 0;
    private float onActionDownTouchY = 0;

    private float onActionDownMinimapPreviewLeft = 0;
    private float onActionDownMinimapPreviewRight = 0;

    ChartUserInteractorImpl(ChartSolver chartSolver) {
        this.chartSolver = chartSolver;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float touchX = event.getX();
        final float touchY = event.getY();

        boolean result = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                result = onTouchDown(touchX, touchY);
                break;

            case MotionEvent.ACTION_UP:
                state = State.NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                result = onTouchMove(touchX, touchY);
                break;
        }

        return result;
    }

    private boolean onTouchDown(float touchX, float touchY) {
        final ChartState chartState = chartSolver.getState();
        final Rect previewRect = chartState.getMinimapPreviewRect();

        onActionDownTouchX = touchX;
        onActionDownTouchY = touchY;

        if (state != State.NONE) {
            return false;
        }

        final int areaSizeHalf = chartState.minimapPreviewResizeAreaSize / 2;

        if (touchX > previewRect.left - areaSizeHalf && touchX < previewRect.left + areaSizeHalf &&
                touchY > previewRect.top && touchY < previewRect.bottom)
        {
            state = State.MINIMAP_RESIZE_LEFT;
            onActionDownMinimapPreviewLeft = chartState.minimapPreviewLeft;
            onActionDownMinimapPreviewRight = chartState.minimapPreviewRight;
            return true;
        }
        else if (touchX > previewRect.right - areaSizeHalf && touchX < previewRect.right + areaSizeHalf &&
                touchY > previewRect.top && touchY < previewRect.bottom)
        {
            state = State.MINIMAP_RESIZE_RIGHT;
            onActionDownMinimapPreviewLeft = chartState.minimapPreviewLeft;
            onActionDownMinimapPreviewRight = chartState.minimapPreviewRight;
            return true;
        }
        else  if (touchX > previewRect.left && touchX < previewRect.right &&
                touchY > previewRect.top && touchY < previewRect.bottom)
        {
            state = State.MINIMAP_MOVE;
            onActionDownMinimapPreviewLeft = chartState.minimapPreviewLeft;
            Log.d("ChartView", "update state to MINIMAP_MOVE");
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    private boolean onTouchMove(float touchX, float touchY) {
        switch (state) {
            case MINIMAP_MOVE:
                return handleMinimapMove(touchX);

            case MINIMAP_RESIZE_LEFT:
                return handleMinimapResizeLeft(touchX);

            case MINIMAP_RESIZE_RIGHT:
                return handleMinimapResizeRight(touchX);

            default:
                return false;
        }
    }

    private boolean handleMinimapMove(float touchX) {
        chartSolver.setMinimapPosition(onActionDownMinimapPreviewLeft + touchX - onActionDownTouchX);
        return true;
    }

    private boolean handleMinimapResizeLeft(float touchX) {
        chartSolver.setMinimapLeft(onActionDownMinimapPreviewLeft + touchX - onActionDownTouchX);
        return true;
    }

    private boolean handleMinimapResizeRight(float touchX) {
        chartSolver.setMinimapRight(onActionDownMinimapPreviewRight + touchX - onActionDownTouchX);
        return true;
    }
}
