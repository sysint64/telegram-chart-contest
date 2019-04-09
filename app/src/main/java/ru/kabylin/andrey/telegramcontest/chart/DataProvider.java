package ru.kabylin.andrey.telegramcontest.chart;

import android.content.res.AssetManager;

import org.json.JSONException;

import java.io.IOException;

public interface DataProvider {
    ChartState getOverview(AssetManager assetManager, int index) throws IOException, JSONException;

    ChartState getZoomed(AssetManager assetManager, int index, long time) throws IOException, JSONException;
}
