package ru.kabylin.andrey.telegramcontest;

import ru.kabylin.andrey.telegramcontest.chart.ChartState;

final class ChartRecyclerItem {
    final ChartViewLayoutManager layoutManager;
    final ChartState chartState;
    final String title;

    ChartRecyclerItem(ChartViewLayoutManager layoutManager, ChartState chartState, String title) {
        this.layoutManager = layoutManager;
        this.chartState = chartState;
        this.title = title;
    }
}
