package ru.kabylin.andrey.telegramcontest;

import ru.kabylin.andrey.telegramcontest.chart.ChartState;

public final class ChartRecyclerItem {
    public final ChartViewLayoutManager layoutManager;
    public final ChartState chartState;

    public ChartRecyclerItem(ChartViewLayoutManager layoutManager, ChartState chartState) {
        this.layoutManager = layoutManager;
        this.chartState = chartState;
    }
}
