package ru.kabylin.andrey.telegramcontest.chart;

import android.content.res.AssetManager;
import android.text.format.DateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.kabylin.andrey.telegramcontest.helpers.ResourcesUtils;

public class JsonDataProvider implements DataProvider {
    @Override
    public ChartState getOverview(AssetManager assetManager, int index) throws IOException, JSONException {
        return loadChartStateFromFile(assetManager, index + "/overview.json", false);
    }

    @Override
    public ChartState getZoomed(AssetManager assetManager, int index, long time) throws IOException, JSONException {
        final Date date = new Date(time);
        final String day = (String) DateFormat.format("dd",   date);
        final String month = (String) DateFormat.format("MM",   date);
        final String year = (String) DateFormat.format("yyyy",   date);
        final String fileName = index + "/" + year + "-" + month + "/" + day + ".json";

        return loadChartStateFromFile(assetManager, fileName, true);
    }

    private ChartState loadChartStateFromFile(AssetManager assetManager, String fileName, boolean showTime) throws IOException, JSONException {
        final String json = ResourcesUtils.readTextAsset(assetManager, fileName);
        final JSONObject jsonObject = new JSONObject(json);
        final JSONArray columns = jsonObject.getJSONArray("columns");

        final ChartState chartState = new ChartState(showTime);

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
