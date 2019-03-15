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

        if (touchX > previewRect.left && touchX < previewRect.left + chartState.minimapPreviewResizeAreaSize &&
                touchY > previewRect.top && touchY < previewRect.bottom)
        {
            state = State.MINIMAP_RESIZE_LEFT;
            onActionDownMinimapPreviewLeft = chartState.minimapPreviewLeft;
            onActionDownMinimapPreviewRight = chartState.minimapPreviewRight;
            return true;
        }
        else if (touchX > previewRect.right - chartState.minimapPreviewResizeAreaSize && touchX < previewRect.right &&
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
        final float delta = touchX - onActionDownTouchX;

        switch (state) {
            case MINIMAP_MOVE:
                chartSolver.setMinimapPosition(onActionDownMinimapPreviewLeft + delta);
                return true;

            case MINIMAP_RESIZE_LEFT:
                chartSolver.setMinimapLeft(onActionDownMinimapPreviewLeft + delta);
                return true;

            case MINIMAP_RESIZE_RIGHT:
                chartSolver.setMinimapRight(onActionDownMinimapPreviewRight + delta);
                return true;

            default:
                return false;
        }
    }
}
