package ru.kabylin.andrey.telegramcontest.chart;

import android.content.res.AssetManager;
import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ru.kabylin.andrey.telegramcontest.helpers.ResourcesUtils;

public class JsonDataProvider implements DataProvider {
    @Override
    public ChartState getOverview(AssetManager assetManager, int index) throws IOException, JSONException {
        final String json = ResourcesUtils.readTextAsset(assetManager, index + "/overview.json");
        final JSONObject jsonObject = new JSONObject(json);

        final ChartState chartState = new ChartState();
        final JSONArray columns = jsonObject.getJSONArray("columns");

        for (int i = 0; i < columns.length(); ++i) {
            final JSONArray items = columns.getJSONArray(i);
            final String name = items.getString(0);

            if (name.equals("x")) {
                final List<Long> xValues = new ArrayList<>();

                for (int j = 1; j < items.length(); ++j) {
                    xValues.add(items.getLong(j));
                }

                chartState.setX(xValues);
            } else {
                final List<Long> yValues = new ArrayList<>();

                for (int j = 1; j < items.length(); ++j) {
                    yValues.add(items.getLong(j));
                }

                final JSONObject colors = jsonObject.getJSONObject("colors");
                final JSONObject names = jsonObject.getJSONObject("names");

                chartState.addChart(colors.getString(name), names.getString(name), yValues);
            }
        }

        return chartState;
    }

    @Override
    public ChartState getZoomed(AssetManager assetManager, int index, long time) throws IOException, JSONException {
        final Date date = new Date(time);
        final String day = (String) DateFormat.format("dd",   date);

        int dayNumber = Integer.valueOf(day);
        final List<Integer> days = new ArrayList<>();

        final int borderRight = dayNumber + 3;
        final int borderLeft = dayNumber - 2;

        int cursor = borderLeft;
        int leftExtra = 0;
        int rightExtra = 0;
        boolean left = false;

        while (cursor <= borderRight) {
            final String fileName = getZoomedFileName(index, time, cursor);

            if (ResourcesUtils.isAssetExists(assetManager, fileName)) {
                left = true;
                days.add(cursor);
            } else {
                if (left) {
                    leftExtra += 1;
                    days.add(borderLeft - leftExtra);
                } else {
                    rightExtra += 1;
                    days.add(borderRight + rightExtra);
                }
            }

            cursor += 1;
        }

        Collections.sort(days);

        final ChartState chartState = new ChartState();

        for (final int currentDay : days) {
            final String fileName = getZoomedFileName(index, time, currentDay);
            loadToChartState(assetManager, chartState, fileName);
        }

        return chartState;
    }

    private String getZoomedFileName(int index, long time, int day) {
        final Date date = new Date(time);
        final String month = (String) DateFormat.format("MM",   date);
        final String year = (String) DateFormat.format("yyyy",   date);

        return index + "/" + year + "-" + month + "/" + formatDay(day) + ".json";
    }

    private String formatDay(int day) {
        if (day < 10) {
            return  "0" + day;
        } else {
            return  String.valueOf(day);
        }
    }

    private void loadToChartState(AssetManager assetManager, ChartState chartState, String fileName) throws IOException, JSONException {
        final String json = ResourcesUtils.readTextAsset(assetManager, fileName);
        final JSONObject jsonObject = new JSONObject(json);

        final JSONArray columns = jsonObject.getJSONArray("columns");

        for (int i = 0; i < columns.length(); ++i) {
            final JSONArray items = columns.getJSONArray(i);
            final String name = items.getString(0);

            if (name.equals("x")) {
                final List<Long> xValues = new ArrayList<>();

                for (int j = 1; j < items.length(); ++j) {
                    xValues.add(items.getLong(j));
                }

                chartState.addX(xValues);
            } else {
                final List<Long> yValues = new ArrayList<>();

                for (int j = 1; j < items.length(); ++j) {
                    yValues.add(items.getLong(j));
                }

                final JSONObject colors = jsonObject.getJSONObject("colors");
                final JSONObject names = jsonObject.getJSONObject("names");

                chartState.addChart(colors.getString(name), names.getString(name), yValues);
            }
        }
    }
}
