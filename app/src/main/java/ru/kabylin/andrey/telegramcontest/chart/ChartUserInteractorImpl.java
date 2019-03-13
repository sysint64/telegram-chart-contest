package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

class ChartUserInteractorImpl implements ChartUserInteractor  {
    enum State {
        NONE,
        MINIMAP_MOVE
    }

    private final ChartSolver chartSolver;
    private State state = State.NONE;

    private float onActionDownTouchX = 0;
    private float onActionDownTouchY = 0;

    private float onActionDownMinimapPreviewPosition = 0;

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

        if (touchX > previewRect.left && touchX < previewRect.right &&
                touchY > previewRect.top && touchY < previewRect.bottom)
        {
            state = State.MINIMAP_MOVE;
            onActionDownMinimapPreviewPosition = chartState.minimapPreviewPosition;
            Log.d("ChartView", "update state to MINIMAP_MOVE");
            return true;
        }

        return false;
    }

    private boolean onTouchMove(float touchX, float touchY) {
        if (state == State.MINIMAP_MOVE) {
            chartSolver.setMinimapPoisiton(onActionDownMinimapPreviewPosition + touchX - onActionDownTouchX);
            return true;
        }
        else {
            return false;
        }
    }
}
