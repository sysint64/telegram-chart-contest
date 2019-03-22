package ru.kabylin.andrey.telegramcontest;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class ChartViewLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    ChartViewLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }

    public void setScrollEnabled(boolean value) {
        isScrollEnabled = value;
    }
}
