package ru.kabylin.andrey.telegramcontest.chart;

import android.view.MotionEvent;

import ru.kabylin.andrey.telegramcontest.ChartViewLayoutManager;

interface ChartUserInteractor {
    boolean onTouchEvent(MotionEvent event);

    void setChartViewLayoutManager(ChartViewLayoutManager layoutManager);

    void setChartSolver(ChartSolver chartSolver);
}
