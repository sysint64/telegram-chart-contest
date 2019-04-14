package ru.kabylin.andrey.telegramcontest.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

import ru.kabylin.andrey.telegramcontest.ChartViewLayoutManager;
import ru.kabylin.andrey.telegramcontest.R;
import ru.kabylin.andrey.telegramcontest.helpers.MathUtils;
import ru.kabylin.andrey.telegramcontest.helpers.MeasureUtils;

public final class ChartView extends View implements OnChartStateRetrieved, PopupOnClickListener {
    private ChartStyle style = new ChartStyle();

    private ChartRenderer chartRendererZoomedOut = new ChartRenderer(style);
    private ChartRenderer chartRendererZoomedIn = new ChartRenderer(style);

    private ChartRenderer currentChartRenderer = chartRendererZoomedOut;
    private final ChartUserInteractor userInteractor = new ChartUserInteractorImpl(currentChartRenderer.chartSolver);

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect minimapOverlayLeftRect = new Rect();
    private Rect minimapOverlayRightRect = new Rect();
    private Rect minimapBorderLeftRect = new Rect();
    private Rect minimapBorderRightRect = new Rect();
    private Rect minimapBorderTopRect = new Rect();
    private Rect minimapBorderBottomRect = new Rect();

    private float minimapPreviewLeft;
    private float minimapPreviewRight;
    private float minimapPreviewLeftState;
    private float minimapPreviewRightState;

    private OnZoomListener onZoomListener = null;

    @SuppressWarnings("FieldCanBeLocal")
    private float minimapPreviewChangeSpeed = 150f;

    private long lastTime = System.nanoTime();

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
        style.chartAxisTextColor = getResources().getColor(chartAxisTextColorRes);

        @ColorRes final int chartGridColorRes = a.getResourceId(R.styleable.ChartView_chartGridColor, R.color.lightThemeChartGrid);
        style.chartGridColor = getResources().getColor(chartGridColorRes);

        @ColorRes final int chartPopupColorRes = a.getResourceId(R.styleable.ChartView_chartPopupColor, R.color.lightThemeChartPopup);
        style.chartColorPopupColor = getResources().getColor(chartPopupColorRes);

        @ColorRes final int chartPopupColorTitleRes = a.getResourceId(R.styleable.ChartView_chartPopupTitleColor, R.color.lightThemeChartPopupTitle);
        style.chartColorPopupTitleColor = getResources().getColor(chartPopupColorTitleRes);

        @ColorRes final int chartPopupColorItemRes = a.getResourceId(R.styleable.ChartView_chartPopupItemColor, R.color.lightThemeChartPopupTitle);
        style.chartColorPopupItemColor = getResources().getColor(chartPopupColorItemRes);

        @ColorRes final int chartPopupLineColorRes = a.getResourceId(R.styleable.ChartView_chartPopupLineColor, R.color.lightThemeChartPopupLine);
        style.chartPopupLineColor = getResources().getColor(chartPopupLineColorRes);

        @ColorRes final int chartMinimapOverlayColorRes = a.getResourceId(R.styleable.ChartView_chartMinimapOverlayColor, R.color.lightThemeChartMinimapOverlay);
        style.chartMinimapOverlayColor = getResources().getColor(chartMinimapOverlayColorRes);

        @ColorRes final int chartMinimapBorderColorRes = a.getResourceId(R.styleable.ChartView_chartMinimapBorderColor, R.color.lightThemeChartMinimapBorder);
        style.chartMinimapBorderColor = getResources().getColor(chartMinimapBorderColorRes);

        @ColorRes final int chartBackgroundColorRes = a.getResourceId(R.styleable.ChartView_chartBackgroundColor, R.color.lightThemeChartBackground);
        style.chartBackgroundColor = getResources().getColor(chartBackgroundColorRes);
    }

    public void setChartState(ChartState chartState) {
        chartState.popup.setPopupOnClickListener(this);

        if (!chartState.normilizeToPercentage) {
            chartState.popup.arrowIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_chevron_right, getContext().getTheme());
            chartState.popup.arrowIcon.mutate();
        }

        chartRendererZoomedOut.setChartState(chartState, getResources());

        if (chartState.zoomed) {
            currentChartRenderer = chartRendererZoomedIn;
            currentChartRenderer.setChartState(chartState.zoomedChartState, getResources());
            userInteractor.setChartSolver(chartRendererZoomedIn.chartSolver);
        }

        minimapPreviewLeftState = chartState.minimapPreviewLeft;
        minimapPreviewRightState = chartState.minimapPreviewRight;
        minimapPreviewLeft = minimapPreviewLeftState;
        minimapPreviewRight = minimapPreviewRightState;
    }

    public void setOnZoomListener(OnZoomListener onZoomListener) {
        this.onZoomListener = onZoomListener;
    }

    public void setOnChangeChartVisibleRangeListener(OnChangeChartVisibleRangeListener onChangeChartVisibleRangeListener) {
        this.chartRendererZoomedOut.chartSolver.setOnChangeChartVisibleRangeListener(onChangeChartVisibleRangeListener);
        this.chartRendererZoomedIn.chartSolver.setOnChangeChartVisibleRangeListener(onChangeChartVisibleRangeListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final long time = System.nanoTime();
        float deltaTime = (time - lastTime) / 1000000f / 10000f;

        if (chartRendererZoomedOut.isInit) {
            chartRendererZoomedOut.onProgress(deltaTime);
        }

        if (chartRendererZoomedIn.isInit) {
            chartRendererZoomedIn.onProgress(deltaTime);
        }

        if (currentChartRenderer != null) {
            minimapOnProgress(deltaTime);
        }

        lastTime = time;

        chartRendererZoomedOut.setSize(getWidth(), getHeight());
        chartRendererZoomedIn.setSize(getWidth(), getHeight());

        chartRendererZoomedOut.onDraw(canvas);
        chartRendererZoomedIn.onDraw(canvas);

        drawMinimapPreview(canvas);

        invalidate();
    }

    private void minimapOnProgress(float deltaTime) {

        ChartState chartState = currentChartRenderer.chartSolver.getState();

        minimapPreviewLeftState = chartState.minimapPreviewLeft;
        minimapPreviewRightState = chartState.minimapPreviewRight;

        minimapPreviewLeft = MathUtils.interpTo(
                minimapPreviewLeft,
                minimapPreviewLeftState,
                deltaTime,
                minimapPreviewChangeSpeed
        );

        minimapPreviewRight = MathUtils.interpTo(
                minimapPreviewRight,
                minimapPreviewRightState,
                deltaTime,
                minimapPreviewChangeSpeed
        );
    }

    private void drawMinimapPreview(Canvas canvas) {
        final ChartState state = currentChartRenderer.chartSolver.getState();
        final Rect previewRect = state.getMinimapPreviewRect();

        if (previewRect == null) {
            return;
        }

        int dp1 = (int) MeasureUtils.convertDpToPixel(1);

        // Overlay
        paint.setColor(style.chartMinimapOverlayColor);

        minimapOverlayLeftRect.set(state.minimapRect.left, state.minimapRect.top, (int) minimapPreviewLeft, state.minimapRect.bottom + dp1);
        minimapOverlayRightRect.set((int) minimapPreviewRight, state.minimapRect.top, state.minimapRect.right, state.minimapRect.bottom + dp1);

        canvas.drawRect(minimapOverlayLeftRect, paint);
        canvas.drawRect(minimapOverlayRightRect, paint);

        // Border
        paint.setColor(style.chartMinimapBorderColor);

        minimapBorderLeftRect.set(
                (int) minimapPreviewLeft,
                previewRect.top,
                (int) minimapPreviewLeft + state.minimapPreviewRenderResizeAreaSize,
                previewRect.bottom + dp1
        );

        minimapBorderRightRect.set(
                (int) minimapPreviewRight - state.minimapPreviewRenderResizeAreaSize,
                previewRect.top,
                (int) minimapPreviewRight,
                previewRect.bottom + dp1
        );

        minimapBorderTopRect.set(
                (int) minimapPreviewLeft + state.minimapPreviewRenderResizeAreaSize,
                previewRect.top,
                (int) minimapPreviewRight - state.minimapPreviewRenderResizeAreaSize,
                previewRect.top + state.minimapPreviewBorderHeight
        );

        minimapBorderBottomRect.set(
                (int) minimapPreviewLeft + state.minimapPreviewRenderResizeAreaSize,
                previewRect.bottom - state.minimapPreviewBorderHeight,
                (int) minimapPreviewRight - state.minimapPreviewRenderResizeAreaSize,
                previewRect.bottom + dp1
        );

        canvas.drawRect(minimapBorderLeftRect, paint);
        canvas.drawRect(minimapBorderRightRect, paint);
        canvas.drawRect(minimapBorderTopRect, paint);
        canvas.drawRect(minimapBorderBottomRect, paint);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = currentChartRenderer.onTouchEvent(event);

        if (result) {
            userInteractor.resetTouch();
            return result;
        } else {
            return userInteractor.onTouchEvent(event);
        }
    }

    public void setLayoutManager(ChartViewLayoutManager layoutManager) {
        userInteractor.setChartViewLayoutManager(layoutManager);
    }

    private static class RetrieveZoomedInStateTask extends AsyncTask<Object, Void, ChartState> {
        final private WeakReference<OnChartStateRetrieved> onChartStateRetrieved;

        RetrieveZoomedInStateTask(OnChartStateRetrieved onChartStateRetrieved) {
            this.onChartStateRetrieved = new WeakReference<>(onChartStateRetrieved);
        }

        @Override
        protected ChartState doInBackground(Object... objects) {
            final ChartSolver chartSolver = (ChartSolver) objects[0];
            final AssetManager assetManager = (AssetManager) objects[1];
            return chartSolver.zoomIn(assetManager);
        }

        @Override
        protected void onPostExecute(ChartState result) {
            if (onChartStateRetrieved.get() != null && result != null) {
                onChartStateRetrieved.get().onChartStateRetrieved(result);
            }
        }
    }

    @Override
    public void onChartStateRetrieved(ChartState chartState) {
        chartRendererZoomedIn.setChartState(chartState, getResources());
        chartRendererZoomedIn.chartSolver.zoomOut();
        currentChartRenderer = chartRendererZoomedIn;
        userInteractor.setChartSolver(currentChartRenderer.chartSolver);
        onZoomListener.onZoomIn();
    }

    public void zoomIn() {
        new RetrieveZoomedInStateTask(this).execute(chartRendererZoomedOut.chartSolver, getContext().getAssets());
    }

    public void zoomOut() {
        currentChartRenderer = chartRendererZoomedOut;
        chartRendererZoomedOut.chartSolver.zoomOut();

        if (chartRendererZoomedIn.isInit) {
            chartRendererZoomedIn.chartSolver.zoomIn();
        }

        userInteractor.setChartSolver(currentChartRenderer.chartSolver);
        onZoomListener.onZoomOut();
    }

    @Override
    public void onPopupClick() {
        zoomIn();
    }
}
