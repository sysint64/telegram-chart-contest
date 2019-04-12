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

public final class ChartView extends View {
    private ChartStyle style = new ChartStyle();

    private ChartRenderer chartRendererZoomOut = new ChartRenderer(style);
    private ChartRenderer chartRendererZoomedIn = new ChartRenderer(style);

    private ChartSolver currentChartSolver = chartRendererZoomOut.chartSolver;
    private final ChartUserInteractor userInteractor = new ChartUserInteractorImpl(currentChartSolver);

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
        chartRendererZoomOut.setChartState(chartState);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (chartRendererZoomOut.onDraw(canvas, getWidth(), getHeight())) {
            invalidate();
        }

        if (chartRendererZoomedIn.onDraw(canvas, getWidth(), getHeight())) {
            invalidate();
        }
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

    public void zoomIn() {
        ChartState zoomedChartState = chartRendererZoomOut.chartSolver.zoomIn(getContext().getAssets());

        if (zoomedChartState != null) {
            chartRendererZoomedIn.setChartState(zoomedChartState);
            chartRendererZoomedIn.chartSolver.zoomOut();
            currentChartSolver = chartRendererZoomedIn.chartSolver;
            userInteractor.setChartSolver(currentChartSolver);
        }
    }

    public void zoomOut() {
        currentChartSolver = chartRendererZoomOut.chartSolver;
        chartRendererZoomOut.chartSolver.zoomOut();
        chartRendererZoomedIn.chartSolver.zoomIn();
        userInteractor.setChartSolver(currentChartSolver);
    }
}
