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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

import ru.kabylin.andrey.telegramcontest.ChartViewLayoutManager;
import ru.kabylin.andrey.telegramcontest.R;
import ru.kabylin.andrey.telegramcontest.helpers.MathUtils;

public final class ChartView extends View implements OnChartStateRetrieved {
    private ChartStyle style = new ChartStyle();

    private ChartRenderer chartRendererZoomedOut = new ChartRenderer(style);
    private ChartRenderer chartRendererZoomedIn = new ChartRenderer(style);

    private ChartSolver currentChartSolver = chartRendererZoomedOut.chartSolver;
    private final ChartUserInteractor userInteractor = new ChartUserInteractorImpl(currentChartSolver);

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
        chartRendererZoomedOut.setChartState(chartState);
        minimapPreviewLeftState = chartState.minimapPreviewLeft;
        minimapPreviewRightState = chartState.minimapPreviewRight;
        minimapPreviewLeft = minimapPreviewLeftState;
        minimapPreviewRight = minimapPreviewRightState;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final long time = System.nanoTime();
        float deltaTime = (time - lastTime) / 1000000f / 10000f;

        if (chartRendererZoomedOut.isInit) {
            chartRendererZoomedOut.chartSolver.onProgress(deltaTime);
        }

        if (chartRendererZoomedIn.isInit) {
            chartRendererZoomedIn.chartSolver.onProgress(deltaTime);
        }

        if (currentChartSolver != null) {
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

        ChartState chartState = currentChartSolver.getState();

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
        final ChartState state = chartRendererZoomedOut.chartSolver.getState();
        final Rect previewRect = state.getMinimapPreviewRect();

        // Overlay
        paint.setColor(style.chartMinimapOverlayColor);

        minimapOverlayLeftRect.set(state.minimapRect.left, state.minimapRect.top, (int) minimapPreviewLeft, state.minimapRect.bottom);
        minimapOverlayRightRect.set((int) minimapPreviewRight, state.minimapRect.top, state.minimapRect.right, state.minimapRect.bottom);

        canvas.drawRect(minimapOverlayLeftRect, paint);
        canvas.drawRect(minimapOverlayRightRect, paint);

        // Border
        paint.setColor(style.chartMinimapBorderColor);

        minimapBorderLeftRect.set(
                (int) minimapPreviewLeft,
                previewRect.top,
                (int) minimapPreviewLeft + state.minimapPreviewRenderResizeAreaSize,
                previewRect.bottom
        );

        minimapBorderRightRect.set(
                (int) minimapPreviewRight - state.minimapPreviewRenderResizeAreaSize,
                previewRect.top,
                (int) minimapPreviewRight,
                previewRect.bottom
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
                previewRect.bottom
        );

        canvas.drawRect(minimapBorderLeftRect, paint);
        canvas.drawRect(minimapBorderRightRect, paint);
        canvas.drawRect(minimapBorderTopRect, paint);
        canvas.drawRect(minimapBorderBottomRect, paint);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        return userInteractor.onTouchEvent(event);
    }

    public boolean setChartVisibilityByName(final String name, final boolean visibility) {
        return currentChartSolver.setChartVisibilityByName(name, visibility);
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
        chartRendererZoomedIn.setChartState(chartState);
        chartRendererZoomedIn.chartSolver.zoomOut();
        currentChartSolver = chartRendererZoomedIn.chartSolver;
        userInteractor.setChartSolver(currentChartSolver);
    }

    public void zoomIn() {
        new RetrieveZoomedInStateTask(this).execute(chartRendererZoomedOut.chartSolver, getContext().getAssets());
    }

    public void zoomOut() {
        currentChartSolver = chartRendererZoomedOut.chartSolver;
        chartRendererZoomedOut.chartSolver.zoomOut();
        chartRendererZoomedIn.chartSolver.zoomIn();
        userInteractor.setChartSolver(currentChartSolver);
    }
}
