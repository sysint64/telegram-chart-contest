package ru.kabylin.andrey.telegramcontest.chart;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import ru.kabylin.andrey.telegramcontest.ChartViewLayoutManager;

import static android.view.MotionEvent.INVALID_POINTER_ID;

class ChartUserInteractorImpl implements ChartUserInteractor {
    enum State {
        NONE,
        POPUP_MOVE,
        MINIMAP_MOVE,
        MINIMAP_RESIZE_LEFT,
        MINIMAP_RESIZE_RIGHT,
    }

    private final ChartSolver chartSolver;
    private State[] state = new State[]{State.NONE, State.NONE};

    private float[] onActionDownTouchX = new float[2];
    private float[] onActionDownTouchY = new float[2];

    private float[] onActionDownMinimapPreviewLeft = new float[2];
    private float[] onActionDownMinimapPreviewRight = new float[2];

    private boolean multiTouch = false;

    private Vertex pointUnderMouse = new Vertex();
    private ChartViewLayoutManager layoutManager = null;

    private int activePointerId = INVALID_POINTER_ID;

    ChartUserInteractorImpl(ChartSolver chartSolver) {
        this.chartSolver = chartSolver;
    }

    public void setChartViewLayoutManager(ChartViewLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        final float touchX = event.getX();
//        final float touchY = event.getY();
//
//        Log.d("ChartView", "Pointers "+ multiTouch);
//
//        if (!chartSolver.getState().isInit) {
//            return false;
//        }
//
//        boolean result = false;
//
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                result = onTouchDown(0, touchX, touchY);
//                break;
//
//            case MotionEvent.ACTION_POINTER_DOWN:
//                result = onTouchDown(1, touchX, touchY);
//                Log.d("ChartView", "POINTER DOWN");
//                multiTouch = true;
//                break;
//
//            case MotionEvent.ACTION_POINTER_UP:
//                multiTouch = false;
//                break;
//
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                state = State.NONE;
//                chartSolver.hidePopup();
//                multiTouch = false;
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                result = onTouchMove(touchX, touchY);
//                break;
//        }
//
//        if (layoutManager != null) {
//            if (result) {
//                layoutManager.setScrollEnabled(false);
//            } else {
//                layoutManager.setScrollEnabled(true);
//            }
//        }
//
//        return result;

        if (!chartSolver.getState().isInit) {
            return false;
        }

        boolean result = false;

        int action = event.getAction() & MotionEvent.ACTION_MASK;
        int pointCount = Math.min(event.getPointerCount(), 2);

        multiTouch = pointCount == 2;

        for (int i = 0; i < pointCount; ++i) {
            final float touchX = event.getX(i);
            final float touchY = event.getY(i);

            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_DOWN:
                    if (state[i] == State.NONE) {
                        result = onTouchDown(i, touchX, touchY);
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    result = onTouchMove(i, touchX, touchY);
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    state[i] = State.NONE;
                    chartSolver.hidePopup();
                    multiTouch = false;
                    break;
            }
        }

        if (layoutManager != null) {
            if (state[0] == State.NONE && state[1] == State.NONE) {
                layoutManager.setScrollEnabled(true);
            } else {
                layoutManager.setScrollEnabled(false);
            }
//            if (result) {
//                layoutManager.setScrollEnabled(false);
//            } else {
//                layoutManager.setScrollEnabled(true);
//            }
        }
//        layoutManager.setScrollEnabled(false);
        return result;
    }

    private boolean onTouchDown(int id, float touchX, float touchY) {
        final ChartState chartState = chartSolver.getState();
        final Rect minimapPreviewRect = chartState.getMinimapPreviewRect();
        final Rect previewRect = chartState.previewRect;

        onActionDownTouchX[id] = touchX;
        onActionDownTouchY[id] = touchY;

        if (state[id] != State.NONE) {
            return false;
        }

        if (touchX > minimapPreviewRect.left && touchX < minimapPreviewRect.left + chartState.minimapPreviewResizeAreaSize &&
                touchY > minimapPreviewRect.top && touchY < minimapPreviewRect.bottom)
        {
            state[id] = State.MINIMAP_RESIZE_LEFT;
            onActionDownMinimapPreviewLeft[id] = chartState.minimapPreviewLeft - onActionDownTouchX[id];
            onActionDownMinimapPreviewRight[id] = chartState.minimapPreviewRight - onActionDownTouchX[id];
            return true;
        }
        else if (touchX > minimapPreviewRect.right - chartState.minimapPreviewResizeAreaSize && touchX < minimapPreviewRect.right &&
                touchY > minimapPreviewRect.top && touchY < minimapPreviewRect.bottom)
        {
            state[id] = State.MINIMAP_RESIZE_RIGHT;
            onActionDownMinimapPreviewLeft[id] = chartState.minimapPreviewLeft - onActionDownTouchX[id];
            onActionDownMinimapPreviewRight[id] = chartState.minimapPreviewRight - onActionDownTouchX[id];
            return true;
        }
        else if (touchX > minimapPreviewRect.left && touchX < minimapPreviewRect.right &&
                touchY > minimapPreviewRect.top && touchY < minimapPreviewRect.bottom)
        {
            state[id] = State.MINIMAP_MOVE;
            onActionDownMinimapPreviewLeft[id] = chartState.minimapPreviewLeft;
            return true;
        }
        else if (touchX > previewRect.left && touchX < previewRect.right &&
                touchY > previewRect.top && touchY < previewRect.bottom)
        {
            state[id] = State.POPUP_MOVE;
            chartSolver.dropPopup(touchX, touchY);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    private boolean onTouchMove(int id, float touchX, float touchY) {
        float delta = touchX - onActionDownTouchX[id];
        final ChartState chartState = chartSolver.getState();

        switch (state[id]) {
            case MINIMAP_MOVE:
                chartSolver.setMinimapPosition(onActionDownMinimapPreviewLeft[id] + delta);
                return true;

            case MINIMAP_RESIZE_LEFT:
                chartSolver.setMinimapLeft(touchX + onActionDownMinimapPreviewLeft[id]);
                return true;

            case MINIMAP_RESIZE_RIGHT:
                chartSolver.setMinimapRight(touchX + onActionDownMinimapPreviewRight[id]);
                return true;

            case POPUP_MOVE:
                chartSolver.dropPopup(touchX, touchY);
                return true;

            default:
                return true;
        }
    }
}
