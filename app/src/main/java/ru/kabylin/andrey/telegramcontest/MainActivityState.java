package ru.kabylin.andrey.telegramcontest;

import ru.kabylin.andrey.telegramcontest.chart.ChartState;

// TODO: chartState можно сериализовать и записать в parcelable
public enum MainActivityState {
    INSTANCE;

    public ChartState chartState = null;
}
