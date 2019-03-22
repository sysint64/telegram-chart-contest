package ru.kabylin.andrey.telegramcontest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.kabylin.andrey.telegramcontest.chart.ChartState;

import java.util.ArrayList;
import java.util.List;

final class ChartJsonLoader {
    static ChartState loadCharts(JSONObject jsonObject) throws JSONException {
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
}
