package ru.kabylin.andrey.telegramcontest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.kabylin.andrey.telegramcontest.chart.ChartState;
import ru.kabylin.andrey.telegramcontest.chart.ChartView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ChartView chartView = null;
    private CheckBox checkBox0 = null;
    private CheckBox checkBox1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chartView = findViewById(R.id.chartView);
        checkBox0 = findViewById(R.id.checkBox0);
        checkBox1 = findViewById(R.id.checkBox1);

        checkBox0.setOnClickListener(this);
        checkBox1.setOnClickListener(this);

        try {
            final String json = readJson();

            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.getJSONObject(4);

            loadCharts(jsonObject, chartView);
        } catch (IOException | JSONException e) {
            Log.e("ChartView", "JSON LOAD ERROR");
        }
    }

    private void loadCharts(JSONObject jsonObject, ChartView chartView) throws JSONException {
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
            }
            else {
                final List<Long> yValues = new ArrayList<>();

                for (int j = 1; j < items.length(); ++j) {
                    yValues.add(items.getLong(j));
                }

                final JSONObject colors = jsonObject.getJSONObject("colors");
                final JSONObject names = jsonObject.getJSONObject("names");

                chartState.addChart(colors.getString(name), names.getString(name), yValues);
            }
        }

        chartView.setChartState(chartState);
    }

    private String readJson() throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.chart_data)));
        String line = reader.readLine();
        final StringBuilder json = new StringBuilder();

        while (line != null) {
            json.append(line);
            line = reader.readLine();
        }

        return json.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkBox0:
                chartView.setChartVisibilityByName("#0", checkBox0.isChecked());
                break;

            case R.id.checkBox1:
                chartView.setChartVisibilityByName("#1", checkBox1.isChecked());
                break;
        }
    }
}
