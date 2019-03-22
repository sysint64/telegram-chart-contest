package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import ru.kabylin.andrey.telegramcontest.ChartViewLayoutManager;

class ChartUserInteractorImpl implements ChartUserInteractor {
    enum State {
        NONE,
        POPUP_MOVE,
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

    private Vertex pointUnderMouse = new Vertex();
    private ChartViewLayoutManager layoutManager = null;

    ChartUserInteractorImpl(ChartSolver chartSolver) {
        this.chartSolver = chartSolver;
    }

    public void setChartViewLayoutManager(ChartViewLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float touchX = event.getX();
        final float touchY = event.getY();

        if (!chartSolver.getState().isInit) {
            return false;
        }

        boolean result = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                result = onTouchDown(touchX, touchY);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                state = State.NONE;
                chartSolver.hidePopup();
                break;

            case MotionEvent.ACTION_MOVE:
                result = onTouchMove(touchX, touchY);
                break;
        }

        if (layoutManager != null) {
            if (result) {
                layoutManager.setScrollEnabled(false);
            } else {
                layoutManager.setScrollEnabled(true);
            }
        }

        return result;
    }

    private boolean onTouchDown(float touchX, float touchY) {
        final ChartState chartState = chartSolver.getState();
        final Rect minimapPreviewRect = chartState.getMinimapPreviewRect();
        final Rect previewRect = chartState.previewRect;

        onActionDownTouchX = touchX;
        onActionDownTouchY = touchY;

        if (state != State.NONE) {
            return false;
        }

        if (touchX > minimapPreviewRect.left && touchX < minimapPreviewRect.left + chartState.minimapPreviewResizeAreaSize &&
                touchY > minimapPreviewRect.top && touchY < minimapPreviewRect.bottom)
        {
            state = State.MINIMAP_RESIZE_LEFT;
            onActionDownMinimapPreviewLeft = chartState.minimapPreviewLeft;
            onActionDownMinimapPreviewRight = chartState.minimapPreviewRight;
            return true;
        }
        else if (touchX > minimapPreviewRect.right - chartState.minimapPreviewResizeAreaSize && touchX < minimapPreviewRect.right &&
                touchY > minimapPreviewRect.top && touchY < minimapPreviewRect.bottom)
        {
            state = State.MINIMAP_RESIZE_RIGHT;
            onActionDownMinimapPreviewLeft = chartState.minimapPreviewLeft;
            onActionDownMinimapPreviewRight = chartState.minimapPreviewRight;
            return true;
        }
        else  if (touchX > minimapPreviewRect.left && touchX < minimapPreviewRect.right &&
                touchY > minimapPreviewRect.top && touchY < minimapPreviewRect.bottom)
        {
            state = State.MINIMAP_MOVE;
            onActionDownMinimapPreviewLeft = chartState.minimapPreviewLeft;
            Log.d("ChartView", "update state to MINIMAP_MOVE");
            return true;
        }
        else if (touchX > previewRect.left && touchX < previewRect.right &&
                touchY > previewRect.top && touchY < previewRect.bottom)
        {
            state = State.POPUP_MOVE;
            chartSolver.dropPopup(touchX, touchY);
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

            case POPUP_MOVE:
                chartSolver.dropPopup(touchX, touchY);
                return true;

            default:
                return false;
        }
    }
}
